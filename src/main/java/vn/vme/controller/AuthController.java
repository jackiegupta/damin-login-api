package vn.vme.controller;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.internal.Utils;

import io.swagger.annotations.ApiOperation;
import vn.vme.common.DateUtils;
import vn.vme.common.JCode.CommonCode;
import vn.vme.common.JCode.UserCode;
import vn.vme.common.JConstants.Status;
import vn.vme.common.URI;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Device;
import vn.vme.entity.Setting;
import vn.vme.entity.Task;
import vn.vme.entity.User;
import vn.vme.entity.UserTask;
import vn.vme.io.game.UserTaskRequest;
import vn.vme.io.user.DeviceVO;
import vn.vme.io.user.UserDO;
import vn.vme.io.user.UserVO;
import vn.vme.repository.DeviceRepository;
import vn.vme.repository.UserRepository;
import vn.vme.repository.UserTaskRepository;
import vn.vme.service.I18nService;
import vn.vme.service.RoleService;
import vn.vme.service.SettingService;
import vn.vme.service.StorageService;
import vn.vme.service.TaskService;
import vn.vme.service.TransactionService;
import vn.vme.service.UserService;

/**
 * @author mtech
 */
@RestController
@RequestMapping(URI.V1)
public class AuthController extends BaseController {
    static Logger log = LoggerFactory.getLogger(AuthController.class.getName());

    @Autowired(required = false)
    public UserService userService;

    @Autowired(required = false)
    public RoleService roleService;

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public UserTaskRepository userTaskRepository;
    
    @Autowired
    public DeviceRepository deviceRepository;

    @Autowired(required = false)
    public StorageService storageService;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    public I18nService i18n;

    @Value("${jwt.clientId}")
    private String jwtClientId;

    @Value("${jwt.clientSecret}")
    private String jwtClientSecret;

    
    @Value("${accessTokenUri}")
    private String accessTokenUri;

    @Autowired
	public TransactionService transactionService;
    
    @Autowired
    public SettingService settingService;
    
    @Autowired(required = false)
	public TaskService taskService;
	
   
    
    @ApiOperation(value = "Auth token user")
    @CrossOrigin(origins = "*")
    @PostMapping(value = URI.OAUTH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity authToken(
    							@RequestHeader(name = "deviceId",required = false) String deviceId,
    							@RequestParam String username, @RequestParam String password,
                                 @RequestParam String grant_type, HttpServletRequest http) throws Exception {
        log.info("User Auth: " + username);
        Enumeration<String> headerNames = http.getHeaderNames();
        boolean authorization = false;
        String basicToken = jwtClientId + ":" + jwtClientSecret;
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(basicToken.getBytes());
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            if (key.equals("authorization")) {
                authorization = http.getHeader(key).equals(basicAuth);
                break;
            }
        }
        if (!authorization) {
            return response(CommonCode.ACCESS_DENY);
        }
        User user = userRepository.findByUserNameOrEmailOrPhone(username, username, username);
        if (user == null) {
            return response(UserCode.USER_NAME_INVALID);
        }else if (user.getStatus().equals(Status.BLOCK.name())) {
            return response(UserCode.USER_BLOCK.name());
        }
       
        // REST oauth/token as normal username/password
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", basicAuth);
        HttpEntity<String> header = new HttpEntity<String>(headers);
        String url = accessTokenUri;
        url += "?grant_type=password";
        url += "&username=" + username;
        url += "&password=" + password;
        try {
            ResponseEntity response = this.restTemplate.postForEntity(url, header, Object.class);
            user.setLoginCount(0); 
            user.setLastLogin(new Date());
            try {
	            //TASK LOGIN
	            Task task = taskService.findByCode("LOGIN");
	            if(task != null) {
	            	UserTaskRequest request = new UserTaskRequest("", user.getId(), 0, 0 ,task.getId(), "", "", "");
	            	Date date = new Date();
	            	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");//05-02-2020
	            	String datetime = formatter.format(date);
	            	request.setUpdateDate(datetime + " 00:00:00");
	            	List<UserTask> list = userTaskRepository.findUserTask(request);
	            	if(list !=null && list.size() > 0) {
	            		log.info("có TASK LOGIN");
	            	}else {
	            		log.info("Chưa có TASK LOGIN");
	            		UserTask userTask = new UserTask();
	            		userTask.setUser(user);
	            		userTask.setTask(task);
	            		userTask.setCreateDate(new Date());
	            		userTask.setUpdateDate(new Date());
	            		userTaskRepository.save(userTask);
	            		
	            		transactionService.saveTask(task, user);
	            		
	            	}
	            }
            } catch (Exception e) {
            	e.printStackTrace();
            }
            
            return response(response.getBody());
        } catch (Exception e) {
        	user.setLoginCount(user.getLoginCount()+1);
            String code = UserCode.PASSWORD_INVALID.code;
			return response(code, msg(code), new UserDO(user.getLoginCount()));
        }finally {
        	userRepository.save(user);
		}
    }
    
    @CrossOrigin(origins = "*")
	@GetMapping(value = URI.DEVICE + URI.LIST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getDevice() throws Exception {
		log.info("Get DEVICE ");
		Date loginTime = DateUtils.addMinutes(-5);
		long loginCount = deviceRepository.countByLoginTimeGreaterThan(loginTime );
		long userCount = userRepository.count();
		
		DeviceVO device = new DeviceVO();
		device.setLoginCount(loginCount);
		device.setUserCount(userCount);
		return response(device);
	}
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value = URI.DEVICE, produces = MediaType.APPLICATION_JSON_VALUE)
   	public ResponseEntity setDevice(@RequestParam (name = "deviceId",required = false) String deviceId,
   			HttpServletRequest http) throws Exception {
   		log.info("Set DEVICE " + deviceId);
   		
   		UserVO userVO  = UserContextHolder.getContext().getCurrentUser(); 
		
   		if(Utils.isEmpty(deviceId)) {
   			deviceId = http.getHeader("X-FORWARDED-FOR");  
   			if (Utils.isEmpty(deviceId)) {  
   				deviceId = http.getRemoteAddr();  
   			}
   		}
   		Device device = deviceRepository.findByDeviceId(deviceId);
   		Date createTime = new Date();
		if (device == null) {
			device = new Device(deviceId);
			device.setLoginTime(createTime);
			device.setCreateDate(createTime);
			
		}else {
			device.setLoginTime(createTime);
		}
		if(userVO != null) {
			device.setUserId(userVO.getId());
			device.setUpdateDate(createTime);
		}
		deviceRepository.save(device);
		
		Date loginTime = DateUtils.addMinutes(-3);
		long loginCount = deviceRepository.countByLoginTimeGreaterThan(loginTime );
		long userCount = userRepository.count();
		
		try {
			//Face số thành viên:
			//1 Tổng Thành viên
			//2 Tổng Onlice
			
			Setting countMember = settingService.findOne(1);
			Setting countOnline = settingService.findOne(2);
			int rand1 = vn.vme.common.Utils.getRandomNumber(1,10);
			loginCount = rand1 + loginCount + (Integer.parseInt(countOnline.getValue()))*loginCount/100;
			userCount = userCount + (Integer.parseInt(countMember.getValue()))*userCount/100;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		DeviceVO deviceVO = new DeviceVO();
		deviceVO.setLoginCount(loginCount);
		deviceVO.setUserCount(userCount);
   		return response(deviceVO);
   	}
}
