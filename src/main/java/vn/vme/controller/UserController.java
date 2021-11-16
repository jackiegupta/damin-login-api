package vn.vme.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.JCode;
import vn.vme.common.JCode.CommonCode;
import vn.vme.common.JCode.UserCode;
import vn.vme.common.JConstants.Status;
import vn.vme.common.URI;
import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Product;
import vn.vme.entity.User;
import vn.vme.exception.AccessDeniedException;
import vn.vme.exception.ExistedDataException;
import vn.vme.exception.InvalidKeyException;
import vn.vme.exception.NotFoundException;
import vn.vme.exception.UserLockedException;
import vn.vme.io.game.ProductRequest;
import vn.vme.io.user.PasswordRequest;
import vn.vme.io.user.UserDO;
import vn.vme.io.user.UserRequest;
import vn.vme.io.user.UserVO;
import vn.vme.io.user.VerifyRequest;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.UserRepository;
import vn.vme.service.ProductService;
import vn.vme.service.StorageService;
import vn.vme.service.UserService;

@RestController
@RequestMapping(URI.V1 + URI.USER)
public class UserController extends BaseController {

	static Logger log = LoggerFactory.getLogger(UserController.class.getName());
	@Autowired(required = false)
	public UserService userService;
	
	@Autowired(required = false)
	public ProductService productService;
	
	@Autowired
	public UserRepository userRepository;
	
	@Autowired(required = false)
	public StorageService storageService;

	@ApiOperation(value = "Register new user")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, User successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, User already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, User already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = UserVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity registerUser(@RequestBody UserRequest request)
			throws Exception {
		log.info("User registration: " + request);
		//super.validateRequest(request);
		User user = userService.createUserNotOTP(request);
		return response(user.getVO());
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.CHECK, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity checkUser(@RequestBody UserRequest request)
			throws Exception {
		log.info("User registration: " + request);
		super.validateRequest(request);
		String userName = request.getUserName();
		if(userName.contains("@")) {
			request.setEmail(userName);
		}
//		else {
//			request.setPhone(userName);
//		}
		String email = request.getEmail();
		String phone = request.getPhone();
		User userByUserName = userService.findByUserName(userName);
		User userByEmail = userService.findByEmail(email);
		User userByPhone = userService.findByPhone(phone);
		if (userByUserName == null && userByEmail==null && userByPhone ==null) {
			return response();
		}else {
			boolean unverify = userByUserName.getStatus().equals(Status.INACTIVE.name());
			if (unverify) {
				log.debug(userName + "đã đăng ký nhưng [" + userName + "] chưa kích hoạt.");
				return response(JCode.UserCode.NOT_ACTIVE);
			}else {
				throw new ExistedDataException("User đã tồn tại " + request);
			}
		}
		
		
	}
	@ApiOperation(value = "Set friend Code")
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.REFER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity referFriend(@RequestBody UserRequest request)	throws Exception {
		log.info("User friend Code: " + request.getFriendCode());
		super.validateRequest(request);
		UserVO userVO = this.getCurrentUser();
		User user = userRepository.findById(userVO.getId()).get();
		String friendCode = user.getFriendCode();
		if(Utils.isNotEmpty(friendCode)) {
			return response(JCode.CommonCode.CONFLICT);
		}
		User friend = userRepository.findByReferCode(request.getFriendCode());
		if(friend == null) {
			return response(JCode.CommonCode.NOT_FOUND);
		}
		if(user.getId() == friend.getId()) {
			return response(JCode.CommonCode.CONFLICT);
		}
		userService.referFriend(user,friend);
		return response();
	}
	
	@ApiOperation(value = "Set friend Code")
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.FRIENDCODE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity friendCode()	throws Exception {
		UserVO userVO = this.getCurrentUser();
		User user = userRepository.findById(userVO.getId()).get();
		String friendCode = user.getFriendCode();
		if(friendCode == null) {
			return response(JCode.CommonCode.NOT_FOUND);
		}
		User friend = userRepository.findByReferCode(friendCode);
		if(friend == null) {
			return response(JCode.CommonCode.NOT_FOUND);
		}
		return response(friend.getVO());
	}
	@ApiOperation(value = "Get current user")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return current user existed", response = HashMap.class),
			@ApiResponse(code = 404, message = "NOT_FOUND, please try again", response = HashMap.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getProfile(OAuth2Authentication user) throws Exception {
		String userName = user.getName();
		log.info("Get user profile "+ userName);
		User currentUser = userService.findByUserNameOrEmailOrPhone(userName,userName, userName);
		return response(userService.load(currentUser.getVO()));
	}
	@ApiOperation(value = "Get current user")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return current user existed", response = HashMap.class),
			@ApiResponse(code = 404, message = "NOT_FOUND, please try again", response = HashMap.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.PROFILE + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getProfileInfo(@ApiParam(value = "Get user by userName", defaultValue = "simbat01") 
									@PathVariable(name = "id") String userName) throws Exception {
		log.info("Get user profile info " + userName);
		User user = userService.findByUserName(userName);
		return response(user.getVO().getDO());
	}
	@ApiOperation(value = "Create new general user")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, User successfully registered ", response = UserVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, User already Registered with Phone Number and Domain name but not yet verify", response = UserVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, User already registered also verified. Go signin.", response = UserVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = UserVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, UserName is used, as in the response", response = UserVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = UserVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createUser(@RequestBody UserRequest request)
			throws Exception {
		log.info("Creating user: " + request);
		User user = userService.createUser(request);
		return response(user.getVO());
	}
	@ApiOperation(value = "Update user")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of user update", response = UserVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateProfile(@ApiParam(value = "Object json user") @RequestBody UserRequest request)
			throws Exception {
		UserVO userVO  = UserContextHolder.getContext().getCurrentUser(); 
		log.info("Update User:" + userVO);
		User existed = userService.findOne(userVO.getId());
		if (existed == null) {
			throw new NotFoundException("User Id [" + userVO.getId() + "] invalid ");
		}
		existed = userService.update(request, existed);
		log.info("Patch User with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "exchange rice")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Successful retrieval of user exchange rice", response = UserVO.class),
		@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.EXCHANGE_RICE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity exchangeRice(@ApiParam(value = "Object json user") @RequestBody ProductRequest request)
			throws Exception {
		UserVO userVO  = UserContextHolder.getContext().getCurrentUser(); 
		log.info("Update User:" + userVO);
		User existed = userService.findOne(userVO.getId());
		if (existed == null) {
			throw new NotFoundException("User Id [" + userVO.getId() + "] invalid ");
		}
		Product product = productService.findOne(request.getId());
		if (product == null) {
			throw new NotFoundException("Product Id [" + request.getId() + "] invalid ");
		}
		existed = userService.exchangeRice(product, existed);
		log.info("Patch User with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Update user")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of user update", response = UserVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.ATTEND, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity updateAttend(@ApiParam(value = "Object json user") @RequestBody UserRequest request)
			throws Exception {
		UserVO userVO  = UserContextHolder.getContext().getCurrentUser(); 
		log.info("Update User:" + userVO);
		User existed = userService.findOne(userVO.getId());
		if (existed == null) {
			throw new NotFoundException("User Id [" + userVO.getId() + "] invalid ");
		}
		existed = userService.updateAttend(request, existed);
		log.info("Patch User with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Update user")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of user update", response = UserVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity updateUser(@ApiParam(value = "Object json user") @RequestBody UserRequest request)
			throws Exception {

		Long id = request.getId();
		log.info("Update User:" + request);
		User existed = userService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("User Id [" + id + "] invalid ");
		}
		
		existed = userService.update(request, existed);
		log.info("Patch User with existed = " + existed);
		return response(existed.getVO());
	}
	
	
	
	@ApiOperation(value = "Update user avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of user update", response = UserVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity updateUserPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json user") @RequestParam(value = "id", required = true) Long id)
			throws Exception {
		log.info("Upload photo:" + id);
		User existed = userService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("User Id [" + id + "] invalid ");
		}

		if (file != null) {
			existed = userService.storePhoto(existed,file);
		}
		existed = userService.save(existed);
		log.info("Patch User with existed = " + existed);
		return response(existed.getVO());
	}

	@ApiOperation(value = "Get User by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return user response  ", response = UserVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity getUser(@ApiParam(value = "Get user by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Long id) throws Exception {
		log.info("Get user by id [" + id + "]");
		User user = userService.findOne(id);
		if (user != null) {
			return response(user.getVO());
		} else {
			throw new NotFoundException("User Id [" + id + "] invalid ");
		}
	}
	@ApiOperation(value = "Get User by nameOrEmail")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return user response  ", response = UserVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.INFO, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity getUserByEmailOrPhone(@ApiParam(value = "Get user by email or phone") 
				@RequestParam(name="nameOrEmail") String nameOrEmail) throws Exception {
		log.info("Get User by param  " + nameOrEmail);
		User user = userService.findByEmail(nameOrEmail);
		if (user != null) {
			return response(user.getVO());
		} else {
			throw new NotFoundException("User [" + nameOrEmail + "] invalid ");
		}
	}

	@ApiOperation(value = "List user by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = UserVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listUser(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String level,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search User");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		UserRequest request = new UserRequest(key, level, status);
		Page<User> result = userRepository.search(request, paging);
		log.info("Search user total elements [" + result.getTotalElements() + "]");
		List<User> contentList = result.getContent();
		List<UserVO> responseList = new ArrayList<UserVO>();

		for (User user : contentList) {
			responseList.add(user.getVO());
		}
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "Delete user")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted user", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and User fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyRole('ROOT', 'ADMIN')")
	public ResponseEntity deleteUser(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) String id) throws Exception {
		log.info("Delete  User [" + id + "]");
		User user = null;
		if (id.length() < 8) {
			user = userService.findOne(Long.parseLong(id));
		} else {
			user = userService.findByUserName(id);
		}
		if (user != null) {
			UserVO currentUser = getCurrentUser();
			if (user.getVO().isRoot() || user.getId() == currentUser.getId()) {
				throw new AccessDeniedException("User id [" + user.getId() + "] have not role ");
			}
			log.info("Delete  User [" + user.getId() + "]");
			userService.delete(user.getId());
			return responseMessage("Deleted  User [" + user.getId() + "] sucessfully");
		} else {
			return response(CommonCode.NOT_FOUND.code, "User id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "Request update Password")
	@ApiResponses({ @ApiResponse(code = 202, message = "Successfully verifed", response = Response.class),
			@ApiResponse(code = 204, message = "Already registered also verified with email and Domain name. Go signin.", response = Response.class),
			@ApiResponse(code = 406, message = "Invalid verification code provided, please try again..", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.VERIFY, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity verifyOtp(@RequestBody VerifyRequest request) throws Exception {
		super.validateRequest(request);
		String userName = request.getUserName();
		User user = userService.findByUserName(userName);
		if (user != null) {
			userService.getResetPasswordCode(user);
			return response(user.getVO());
		} else {
			throw new NotFoundException("User userName [" + userName + "] invalid ");
		}
	}
	
	@ApiOperation(value = "Unsubrcribe user email")
	@ApiResponses({ @ApiResponse(code = 202, message = "Successfully verifed", response = Response.class),
			@ApiResponse(code = 204, message = "Already registered also verified with email and Domain name. Go signin.", response = Response.class),
			@ApiResponse(code = 406, message = "Invalid verification code provided, please try again..", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.EMAIL, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity unsubscribeEmail(@RequestParam String email, @RequestParam String hid) throws Exception {
		User user = userService.findByEmail(email);
		if (user != null) {
			if(user.getVO().getHash().equals(hid)) {
			user.setNotify(false); 
			userService.save(user);
				return responseMessage("Hủy bỏ nhận email thành công");
			}else {
				return responseMessage("Có lỗi xảy ra, không thể hủy bỏ nhận email");
			}
		} else {
			throw new NotFoundException("User email [" + email + "] invalid ");
		}
	}
	@ApiOperation(value = "Create user user admin")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successfully", response = UserVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity setPassword(@RequestBody PasswordRequest request) throws Exception {
		super.validateRequest(request);
		User user = null;
		String userName = request.getUserName();
		try {
			user = userService.setPassword(request);
			return response(user.getVO());
		} catch (UserLockedException e) {
			user = userService.findByUserName(userName);
            String code = UserCode.PIN_FAIL_LOCK.code;
			return response(code, e.getMessage(), new UserDO(user.getLoginCount()));
		}catch (InvalidKeyException e) {
			user = userService.findByUserName(userName);
			String code = UserCode.PIN_FAIL.code;
			return response(code, e.getMessage(), new UserDO(user.getLoginCount()));
		}
		
		
	}
	@ApiOperation(value = "Change password")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successfully", response = UserVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.PASSWORD + URI.VERIFY, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity changePassword(@RequestBody PasswordRequest request) throws Exception {
		super.validateRequest(request);
		User user = userService.setPassword(request);
		return response(user.getVO());
	}
	
	@ApiOperation(value = "Change password")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successfully", response = UserVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.CHANGE_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity updatePassword(@RequestBody PasswordRequest request) throws Exception {
		super.validateRequest(request);
		User user = userService.changePassword(request);
		return response(user.getVO());
	}
	
	@ApiOperation(value = "Resend code Email")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successfully resend", response = UserVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.VERIFY, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity resendCode(@RequestBody VerifyRequest request) {
		super.validateRequest(request);
		String userName = request.getUserName();
		User user = userService.findByUserName(userName);
		
		log.info("Get user by findBy UserName [" + userName + "]");
		if (user != null) {
			userService.resendCode(user);
			return response(user.getVO());
		} else {
			throw new NotFoundException("User email [" + userName + "] invalid ");
		}
	}
	
	
	//Admin
	
	 @ApiOperation(value = "Request Reset password by Email")
	    @ApiResponses({
	            @ApiResponse(code = 200, message = "OK, Successful verify otp reset password to user", response = UserVO.class),
	            @ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
	            @ApiResponse(code = 500, response = Response.class, message = "Internal server error")})
	    @CrossOrigin(origins = "*")
	    @PostMapping(value = URI.PASSWORD + URI.EMAIL, produces = MediaType.APPLICATION_JSON_VALUE)
	 @PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
		public ResponseEntity requestResetPasswordOtp(@RequestBody UserRequest request,
				@RequestHeader(name = "deviceId") String deviceId) throws Exception {
	        log.info("Check Reset password ");
	        String userName = request.getEmail();
	        userService.requestResetPassword(userName, deviceId);
	        VerifyRequest verifyVO = new VerifyRequest();
	        verifyVO.setUserName(userName);
	        return response(verifyVO);
	    }
}
