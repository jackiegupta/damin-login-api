package vn.vme.security;

import java.util.Base64;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.vme.entity.User;
import vn.vme.io.user.UserVO;
import vn.vme.repository.UserRepository;

@Service
public class TokenBuilder{

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${jwt.clientId}")
	private String jwtClientId;
	
	@Value("${jwt.clientSecret}")
	private String jwtClientSecret;
	
	@Value("${accessTokenUri}")
	private String accessTokenUri;
	
	public Map<String, Object> createJWT(UserVO userVO) throws Exception {
	    String token = RandomStringUtils.randomAlphanumeric(8);
		User user = userRepository.findById(userVO.getId()).get();
		user.setPassword(new BCryptPasswordEncoder().encode(token));
		userRepository.save(user);
		
		//REST oauth/token as normal username/password
		return authToken(userVO, token);
	}
	private Map<String, Object> authToken(UserVO userVO, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String basicToken = jwtClientId + ":" + jwtClientSecret;
		headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(basicToken.getBytes()));
		HttpEntity<String> header = new HttpEntity<String>(headers);
		String url = accessTokenUri;
		url += "?grant_type=password";
		url += "&username=" + userVO.getUserName();
		url += "&password=" + token;
		ResponseEntity response = this.restTemplate.postForEntity(url, header, Object.class);
		Map<String, Object> body = (Map<String, Object>) response.getBody();
		return body;
	}
}