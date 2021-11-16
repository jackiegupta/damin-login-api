package vn.vme.service;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.vme.common.DateUtils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.User;
import vn.vme.entity.UserGame;
import vn.vme.io.rest360.*;
import vn.vme.io.user.UserVO;
import vn.vme.repository.UserRepository;

@Service
public class Rest360Service {
	private static final Logger log = LoggerFactory.getLogger(Rest360Service.class);

	@Value("${game360.rest}")
	private String rest;
	
	@Value("${game360.username}")
	private String username;
	@Value("${game360.pswd}")
	private String pswd;

	@Autowired
	private Environment env;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	RestTemplate restTemplate;
	Rest360 rest360;
	HttpHeaders headers;
	public Rest360Service() {
	}
	
	@PostConstruct
	public void getToken() {
		log.debug("getToken");
		Rest360 req = new Rest360(username, pswd);
		req.setRequestId("VME"+ DateUtils.getTimeNow());
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(req, headers);
		try {
			rest360 = restTemplate.exchange(rest + "/token", HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<Rest360>() {}).getBody();
			log.debug("response token " + rest360.getErrorCode());
		} catch (Exception e) {
			log.warn("getToken " + e.getMessage());
		}
	}
	public User360 register(User user) {
		User360 request = new User360(user);
		if (user.getUserName().contains("@")) {
			request.setEmail(user.getUserName());
			request.setUsername("");
		}
		log.debug("register");
		request.setRequestId("VME"+ DateUtils.getTimeNow());
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("access_token", rest360.getAccessToken());
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(request, headers);
		try {
			Rest360<User360> response = restTemplate.exchange(rest + "/register", HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<Rest360<User360>>() {}).getBody();
			log.debug("response register " + response.getRequestId());
			if(response.getErrorCode()==0 || response.getErrorCode()== -218) {
				return response.getData();
				//return login(user);
			}else {
				log.debug("register error " + response.getErrorDesc());
				return null;
			}
			
		} catch (Exception e) {
			log.warn("getToken " + e.getMessage());
			return null;
		}
	}
	public User360 login(User user) {
		User360 request = new User360(user);
		log.debug("login");
		request.setRequestId("VME"+ DateUtils.getTimeNow());
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("access_token", rest360.getAccessToken());
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(request, headers);
		try {
			Rest360<User360> response = restTemplate.exchange(rest + "/login", HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<Rest360<User360>>() {}).getBody();
			log.debug("response login " + response.getRequestId());
			if(response.getErrorCode()==0) {
				return response.getData();
			}else {
				log.debug("login error " + response.getErrorDesc());
				return null;
			}
		} catch (Exception e) {
			log.warn("getToken " + e.getMessage());
			return null;
		}
	}
	public Game360 transfer(UserGame game, Integer amount) {
		Game360 request = new Game360(game.getGame().getId(), game.getId360(), amount);
		log.debug("transfer");
		request.setRequestId("VME"+ DateUtils.getTimeNow());
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("access_token", rest360.getAccessToken());
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(request, headers);
		try {
			Rest360<Game360> response = restTemplate.exchange(rest + "/tranfer", HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<Rest360<Game360>>() {}).getBody();
			log.debug("transfer response " + response.getRequestId());
			log.debug("transfer response " + response.getErrorDesc());
			if(response.getErrorCode()==0) {
				return response.getData();
			}else {
				return null;
			}
		} catch (Exception e) {
			log.warn("transfer " + e.getMessage());
			return null;
		}
	}
	
	public User360 updateProfile(User user) {
		User360 request = new User360(user);
		log.debug("updateProfile");
		request.setRequestId("VME"+ DateUtils.getTimeNow());
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("access_token", rest360.getAccessToken());
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(request, headers);
		try {
			Rest360<User360> response = restTemplate.exchange(rest + "/update-profile", HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<Rest360<User360>>() {}).getBody();
			log.debug("response updateProfile " + response.getRequestId());
			if(response.getErrorCode()==0) {
				return response.getData();
			}else {
				log.debug("updateProfile error " + response.getErrorDesc());
				return null;
			}
		} catch (Exception e) {
			log.warn("updateProfile " + e.getMessage());
			return null;
		}
	}
	
	public User360 sendOTP(String phone) {
		User360 request = new User360();
		request.setMsisdn(phone);
		log.debug("login");
		request.setRequestId("VME"+ DateUtils.getTimeNow());
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("access_token", rest360.getAccessToken());
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(request, headers);
		try {
			Rest360<User360> response = restTemplate.exchange(rest + "/send-opt", HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<Rest360<User360>>() {}).getBody();
			log.debug("response sendOTP " + response.getRequestId());
			if(response.getErrorCode()==0) {
				return response.getData();
			}else {
				log.debug("sendOTP error " + response.getErrorDesc());
				return null;
			}
		} catch (Exception e) {
			log.warn("getToken " + e.getMessage());
			return null;
		}
	}
	public User360 checkOTP(String phone, String otp) {
		User360 request = new User360();
		request.setMsisdn(phone);
		request.setOtp(otp);
		log.debug("checkOTP");
		request.setRequestId("VME"+ DateUtils.getTimeNow());
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("access_token", rest360.getAccessToken());
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(request, headers);
		try {
			Rest360<User360> response = restTemplate.exchange(rest + "/check-opt", HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<Rest360<User360>>() {}).getBody();
			log.debug("response checkOTP " + response.getRequestId());
			if(response.getErrorCode()==0) {
				return response.getData();
			}else {
				log.debug("checkOTP error " + response.getErrorDesc());
				return null;
			}
		} catch (Exception e) {
			log.warn("getToken " + e.getMessage());
			return null;
		}
	}
	
	public Object listPackage() {
		log.debug("listPackage");
		this.getToken();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("access_token", rest360.getAccessToken());
		HttpEntity<Object> requestEntity = new HttpEntity<Object>("", headers);
		Object response = null;
		try {
			/*
			Rest360<Package360> response = restTemplate.exchange(rest + "/list-package", HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<Rest360<Package360>>() {}).getBody();
			log.debug("response list-package " + response.getRequestId());
			if(response.getErrorCode()==0) {
				return response;
			}else {
				log.debug("list-package error " + response.getErrorDesc());
				return null;
			}*/
			response = restTemplate.exchange(rest + "/list-package", HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<Object>() {}).getBody();
			log.debug("response checkOTP ");
			/*
			JSONObject jsonObject = (JSONObject) JSONValue.parse(new ObjectMapper().writeValueAsString(response));
			 * if(jsonObject.getErrorCode()==0) { if(response.getErrorCode()==0) { return
			 * response.getData(); }else { log.debug("checkOTP error " +
			 * response.getErrorDesc()); return null; }
			 */
		} catch (Exception e) {
			log.warn("getToken " + e.getMessage());
			return null;
		}
		return response;
	}

	public User360 addRice(double amount) {
		UserVO userVO = UserContextHolder.getContext().getCurrentUser();
		AddRiceRequest addRiceRequest = new AddRiceRequest();
		addRiceRequest.setUserId(userVO.getId());
		addRiceRequest.setAmount((int)amount);
		addRiceRequest.setRequestId("VME"+ DateUtils.getTimeNow());
		addRiceRequest.setProvider(env.getProperty("alepay.provider"));

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("access_token", rest360.getAccessToken());
		HttpEntity<Object> requestEntity = new HttpEntity<>(addRiceRequest, headers);
		System.out.println(requestEntity);
		Rest360<User360> response = null;
		try {
			response = restTemplate.exchange(env.getProperty("game360.rest") + "/gao/add", HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<Rest360<User360>>() {}).getBody();
			if (response.getErrorCode() == 0){
				User user = userRepository.findById(userVO.getId()).get();
				user.setRice(response.getData().getGao());
				userRepository.save(user);
			}
		} catch (Exception e) {
			log.error("addRice ", e.getMessage());
		}
		return response.getData();
	}
}
