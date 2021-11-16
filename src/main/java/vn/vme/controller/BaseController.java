package vn.vme.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import vn.vme.common.JCode;
import vn.vme.common.JCode.CommonCode;
import vn.vme.common.JCode.UserCode;
import vn.vme.common.JConstants;
import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.exception.AccessDeniedException;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Rest;

/**
 * Common function verify Token, load Session Token Format request, response
 * REST API Switch schema DB by tenant Token
 *
 */
@RestController
public class BaseController {
	private static Logger log = LoggerFactory.getLogger(BaseController.class);
	protected String langCode = null;
	protected HttpStatus status;
	protected static final String PAGE = String.valueOf(JConstants.PAGE);
	protected static final String PAGE_SIZE = String.valueOf(JConstants.SIZE);
	@Autowired
	protected MessageSource msg;

	@Autowired
	protected Environment env;

	private Validator validator;
	public HttpServletRequest http;
	public Date requestTime;
	@Value("${serviceUrl}")
	String serviceUrl;
	@Autowired
	protected ApplicationContext context;
	
	public BaseController() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	public String msg(String code) {
		return msg.getMessage(code, null, new Locale("vi"));
	}

	public String msg(String code, Object param) {
		return msg.getMessage(code, new String[] { param.toString() }, new Locale("vi"));
	}

	public String msg(String code, Object[] params) {
		return msg.getMessage(code, params, new Locale("vi"));
	}

	public ResponseEntity responseData(List<?> responseList, Page result) throws Exception {
		Map<String, Object> map = Utils.responseMap(responseList, result);
		return response(map);
	}

	/**
	 * Return REST with List object
	 * 
	 * @param responseList
	 * @param result
	 */
	public ResponseEntity responseList(List<?> responseList, Page result, Long total) {
		Map<String, Object> map = Utils.responseMap(responseList, result, total);
		return response(map);
	}
	
	public ResponseEntity responseList(List<?> responseList, Page result) {
		Map<String, Object> map = Utils.responseMap(responseList, result);
		return response(map);
	}

	public ResponseEntity responseList(List<?> responseList) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(JConstants.DATA_LIST, responseList);
		Paging paging = new Paging();
		paging.setTotalRows(responseList.size());
		map.put(Paging.class.getSimpleName().toLowerCase(), paging);
		return response(map);
	}

	protected ResponseEntity response() {
		return response(JCode.SUCCESS, msg(JCode.SUCCESS));
	}

	protected ResponseEntity success() {
		return response(JCode.SUCCESS, msg(JCode.SUCCESS));
	}

	protected ResponseEntity responseMessage(String message) {
		return response(JCode.SUCCESS, message);
	}

	protected ResponseEntity response(Object data) {
		if (data instanceof String) {
			return response(JCode.SUCCESS, data.toString());
		} else if (data instanceof CommonCode) {
			String code = CommonCode.valueOf(data.toString()).code;
			return response(code, msg(code));
		} else if (data instanceof UserCode) {
			String code = UserCode.valueOf(data.toString()).code;
			return response(code, msg(code));
		} else {
			return response(JCode.SUCCESS, data);
		}
	}

	protected ResponseEntity response(String status, String message) {
		log.debug("ResponseEntity: [" + status + "] [" + message + "]");
		Rest rest = new Rest(status, message);
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity(rest, headers, HttpStatus.OK);
	}

	protected ResponseEntity response(String status,  Object data) {
		log.debug("ResponseEntity: " + status + " [" + data + "]");
		Rest rest = new Rest(status, "OK");
		rest.setData(data);
		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity(rest, headers, HttpStatus.OK);
	}

	protected ResponseEntity response(String status, String message, Object data) {
		log.debug("ResponseEntity: " + status + " [" + status + "]");
		Rest rest = new Rest(status, message);
		rest.setData(data);
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity(rest, headers, HttpStatus.OK);
	}

	/**
	 * POINT data + status + error code
	 * 
	 * @param pointRest
	 * @return
	 * @throws Exception
	 */
	/*
	 * protected ResponseEntity<PointRest> responseEntity(PointRest pointRest)
	 * throws Exception { log.debug("ResponseEntity: " + pointRest);
	 * pointRest.setRequestTime(requestTime); HttpHeaders headers = new
	 * HttpHeaders();
	 * 
	 * return new ResponseEntity<PointRest>(pointRest, headers, HttpStatus.OK); }
	 * protected ResponseEntity<PointRest> responseData(Object data) throws
	 * Exception { PointRest pointRest = new PointRest(data);
	 * pointRest.setRequestTime(requestTime); pointRest.setResponseTime(new Date());
	 * if (http != null) { pointRest.setApi(http.getRequestURI()); }
	 * log.debug("ResponseEntity: " + pointRest); HttpHeaders headers = new
	 * HttpHeaders();
	 * 
	 * return new ResponseEntity<PointRest>(pointRest, headers, HttpStatus.OK); }
	 */
	public void validateUser() throws AccessDeniedException {
		UserVO currentUser = UserContextHolder.getContext().getCurrentUser();
		if (currentUser == null) {
			throw new AccessDeniedException("AccessDeniedException");
		}

	}

	protected UserVO getCurrentUser() throws AccessDeniedException {
		UserVO currentUser = UserContextHolder.getContext().getCurrentUser();
		if (currentUser == null) {
			throw new AccessDeniedException("AccessDeniedException");
		}
		return currentUser;
	}

	 private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	/**
	 * Validate the request Object JSON
	 * 
	 * @param t
	 */
	public <T> void validateRequest(T t, HttpServletRequest http) {
		this.http = http;
		validateRequest(t);
	}

	public void init(HttpServletRequest http) {
		requestTime = new Date();
		this.http = http;
	}

	public <T> void validateRequest(T t) {
		requestTime = new Date();
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
		if (constraintViolations.size() > 0) {
			// Validation failed, build error set and throw invalid input error
			Set<String> errors = constraintViolations.stream().map(c -> c.getMessage()).collect(Collectors.toSet());
			String error = "";
			for (String e : errors) {
				error = error + msg(e) + ".";
			}
			throw new IllegalArgumentException(error);
		}
	}

	public void validate(HttpServletRequest request) {
		log.debug("Starting validate sign" + request.getRequestURL().toString());
		String sign = request.getParameter("sign");
		log.debug("Sign " + sign);
	}

}
