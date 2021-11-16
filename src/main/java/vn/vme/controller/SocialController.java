package vn.vme.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.JCode;
import vn.vme.common.JConstants.Social;
import vn.vme.context.UserContextHolder;
import vn.vme.common.URI;
import vn.vme.common.Utils;
import vn.vme.entity.User;
import vn.vme.exception.AccessDeniedException;
import vn.vme.exception.NotFoundException;
import vn.vme.io.user.SocialRequest;
import vn.vme.io.user.SocialVO;
import vn.vme.io.user.UserRequest;
import vn.vme.io.user.UserVO;
import vn.vme.model.Response;
import vn.vme.repository.UserRepository;
import vn.vme.security.TokenBuilder;
import vn.vme.service.StorageService;
import vn.vme.service.UserService;

@RestController
@RequestMapping(URI.V1 + URI.SOCIAL)
public class SocialController extends BaseController {

	static Logger log = LoggerFactory.getLogger(SocialController.class.getName());
	@Autowired
	HttpServletResponse response;

	@Value("${google.clientId}")
	String googleClientId;

	@Value("${webUrl}")
	String webUrl;
	
	@Value("${facebook.tokenUrl}")
	String facebookTokenUrl;

	@Value("${fileUploadPath}")
	String fileUploadPath;
	
	@Autowired
	public UserService userService;
	@Autowired
	public UserRepository userRepository;
	
	@Autowired
	public TokenBuilder tokenBuilder;
	
	
	@Value("${imageUrl}")
	private String imageUrl;
	
	@Autowired
	StorageService storageService;
	
	
	@ApiOperation(value = "UI có token của google rồi, giờ gọi hàm này để server virify và gen token của mình, "
			+ "nếu user của chưa bao giờ login băng google thì sẽ store để mapping với user của hệ thống")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity loginSocial(@RequestBody SocialRequest socialRequest) throws Exception {
		log.debug("Social Request {{}}", socialRequest);
		String provider = socialRequest.getProvider();
		User user = new User(socialRequest);
		user.setDeviceId(socialRequest.getDeviceId());
		String email = socialRequest.getEmail();
		String externalId = socialRequest.getId();
		if (Utils.isEmpty(email)) {
			throw new AccessDeniedException("No User Email");
		}
		if (provider.equalsIgnoreCase(Social.GOOGLE.name())) {
			JacksonFactory jacksonFactory = new JacksonFactory();
			NetHttpTransport transport = new NetHttpTransport();
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
					.setAudience(Collections.singletonList(googleClientId)).build();

			GoogleIdToken idToken = verifier.verify(socialRequest.getIdToken());
			if (idToken != null) {
				Payload payload = idToken.getPayload();
				String userId = payload.getSubject();
				if (!userId.equals(socialRequest.getId())) {
					throw new AccessDeniedException("Invalid User ID");
				}
				String fullName = (String) payload.get("family_name") + " " + (String) payload.get("given_name");
				user.setFullName(fullName.toUpperCase());
				user.setPhoto((String) payload.get("picture"));
			} else {
				throw new AccessDeniedException("Invalid ID token.");
			}
		} else if (provider.equalsIgnoreCase(Social.FACEBOOK.name())) {
			String validateUrl = facebookTokenUrl + socialRequest.getAuthToken();
			String response = Request.Get(validateUrl).execute().returnContent().asString();

			log.debug("response {{}}", response);

			ObjectMapper mapper = new ObjectMapper();
			SocialVO userInfo = mapper.readValue(response, SocialVO.class);

			if (userInfo == null || userInfo.getId() == null || userInfo.getId().isEmpty()) {
				throw new AccessDeniedException("Invalid Access Token");
			}
			if (!userInfo.getId().equals(socialRequest.getId())) {
				throw new AccessDeniedException("Invalid User Id");
			}
			user.setPhoto(socialRequest.getPhotoUrl());
			user.setFullName(socialRequest.getName() != null ? socialRequest.getName().toUpperCase(): "");
		} else {
			throw new AccessDeniedException("This login Type is not supported:" + provider);
		}

		Map<String, Object> token = null;
		char prefix = provider.toLowerCase().charAt(0);
		User existedUsername = userService.findByUserName(email);
		User existedEmail = userService.findByEmail(email);
		
		if (existedUsername == null && existedEmail == null) {
			//String userName = StringUtils.split(email, "@")[0];
			//if (userService.findByUserName(userName) != null) {
			//	int social = Social.valueOf(provider).ordinal() + 1;
			//	userName = userName + "." + social;
			//}
			user.setUserName(email);
			user.setEmail(email);
			user.setProvider(provider);
			user.setExternalId(externalId);
			user.setDefault();
			user.setReferCode(Utils.genReferCode(6));
			user.setLastLogin(new Date());
			String photoId = prefix + "-" + externalId;
			downloadPhoto(photoId, user);
			userRepository.save(user);
			token = tokenBuilder.createJWT(user.getVO());
		} else {//FACEBOOK - GOOGLE
			if(existedUsername.getProvider() != null && (existedUsername.getProvider().equalsIgnoreCase(Social.GOOGLE.name()) || existedUsername.getProvider().equalsIgnoreCase(Social.FACEBOOK.name()))) {
				existedUsername.setLastLogin(new Date());
				userRepository.save(existedUsername);
				token = tokenBuilder.createJWT(existedUsername.getVO());
			}else {
				return response(JCode.UserCode.EMAIL_EXISTED.code, "Địa chỉ email: "+email+" đã tồn tại ");
			}
		}
		return response(token);
	}
	
	@ApiOperation(value = "Update user")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Successful retrieval of user update", response = UserVO.class),
		@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.LINK_SOCIAL, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT') or hasAnyAuthority('ADMIN') or hasAnyAuthority('GM') or hasAnyAuthority('USER')")
	public ResponseEntity linkSocial(@RequestBody SocialRequest socialRequest)
			throws Exception {
		
		UserVO currentUser = UserContextHolder.getContext().getCurrentUser();
		if(currentUser == null) {
			throw new NotFoundException("User Id invalid ");
		}
		User user = userRepository.findById(currentUser.getId()).get();
		
		String provider = socialRequest.getProvider();
		user.setDeviceId(socialRequest.getDeviceId());
		String email = socialRequest.getEmail();
		String externalId = socialRequest.getId();


		if (provider.equalsIgnoreCase(Social.FACEBOOK.name())) {
			String validateUrl = facebookTokenUrl + socialRequest.getAuthToken();
			String response = Request.Get(validateUrl).execute().returnContent().asString();

			log.debug("response {{}}", response);

			ObjectMapper mapper = new ObjectMapper();
			SocialVO userInfo = mapper.readValue(response, SocialVO.class);

			if (userInfo == null || userInfo.getId() == null || userInfo.getId().isEmpty()) {
				throw new AccessDeniedException("Invalid Access Token");
			}
			if (!userInfo.getId().equals(socialRequest.getId())) {
				throw new AccessDeniedException("Invalid User Id");
			}
			user.setPhoto(socialRequest.getPhotoUrl());
			user.setFullName(socialRequest.getName() != null ? socialRequest.getName().toUpperCase(): "");
		} else {
			throw new AccessDeniedException("This login Type is not supported:" + provider);
		}
		
		user.setEmail(email);
		//user.setProvider("LINK_" + provider);
		user.setExternalId(externalId);
		user.setDefault();
		user.setLastLogin(new Date());
		if(user.getReferCode() == null) {
			user.setReferCode(Utils.genReferCode(6));
		}
		char prefix = provider.toLowerCase().charAt(0);
		String photoId = prefix + "-" + externalId;
		downloadPhoto(photoId, user);
		userRepository.save(user);
		return response(user.getVO());
	}

	private void downloadPhoto(String fileName, User user) {
		String photoUrl = user.getPhoto();
		try {
			
			File file = new File(fileUploadPath + "/" + fileName);
			FileUtils.copyURLToFile(new URL(photoUrl), file);
			String folder = User.class.getSimpleName().toLowerCase();
			
			String fileType = Files.probeContentType(file.toPath()).replace("image/", "");
			if(!fileName.contains(".")) {
				fileName +="."+ fileType;
			}
			InputStream inputStream = new FileInputStream(file);
			Files.copy(inputStream, Paths.get(fileUploadPath + folder).resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
			user.setPhoto(imageUrl + folder + File.separator + fileName);
		}catch (Exception e) {
			log.warn("copyURLToFile" + e.getMessage());
		}
	}
}
