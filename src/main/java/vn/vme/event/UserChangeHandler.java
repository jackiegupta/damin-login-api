package vn.vme.event;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import vn.vme.common.JConstants;
import vn.vme.entity.Email;
import vn.vme.exception.EmailException;
import vn.vme.exception.SmsException;
import vn.vme.io.user.UserVO;
import vn.vme.io.user.VerifyRequest;
import vn.vme.service.EmailService;
import vn.vme.service.SmsService;
import vn.vme.service.TemplateFile;


@Service
public class UserChangeHandler {

	private static final Logger log = LoggerFactory.getLogger(UserChangeHandler.class);
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private  SmsService smsService;
    
    @Autowired
	Environment env;
	
    @Value("${webUrl}")
    String webUrl;
    
    @Value("${serviceUrl}")
    String serviceUrl;
    
	String unsubscribe = serviceUrl + "/v1/user/email?email=";
    
   // @StreamListener("userChangeChanel")
    public void loggerSink(UserChangeModel userChange) throws EmailException, SmsException {
        log.debug("Received a message of type " + userChange.getType());
      
        String action = userChange.getAction();
		log.debug("Received a action {} message from userId id {}", action, userChange.getUser().getId());
		switch(action){
            case JConstants.GET_EVENT:
                break;
            case JConstants.CREATE_EVENT:
                //sendVerifyRegister(userChange);
                break;
            case JConstants.UPDATE_EVENT:
            	//sendUpdatePassword(userChange);
                break;
                
            case JConstants.DELETE_EVENT:
                break;
            case JConstants.VERIFY_PHONE_EVENT:
            	sendVerifyCode(userChange);
                break;    
            default:
                log.error("Received an UNKNOWN user from the user service of type {}", action);
                break;
        }
    }
   
	public void sendUpdatePassword(UserVO user,VerifyRequest verifyRequest) throws EmailException {
    	Long id = user.getId();
		log.debug("Sending update password user", id);
		Email emailRequest = new Email();
		emailRequest.setSubject("Thay đổi mật khẩu");
		String email = user.getEmail();
		emailRequest.setReceiver(email);

		Map<String, String> emailMap=new HashMap<String, String>();
		emailMap.put("name", user.getUserName());
		emailMap.put("verifyCode", verifyRequest.getVerifyCode());
		
		String content= TemplateFile.render("VerifyOtp.html", emailMap);
		emailService.sendAsync(email, "[Gaka] Xác nhận thay đổi mật khẩu", content);
	}
	 
	public void sendVerifyRegister(UserVO user ,VerifyRequest verifyRequest) throws EmailException {
    	Long id = user.getId();
		log.debug("Sending Verify Email user", id);
		Email emailRequest = new Email();
		emailRequest.setSubject("Register User verification");
		String email = user.getEmail();
		emailRequest.setReceiver(email);
		//send email
		String link = webUrl+"/#/verify-email?email="+email+"&verifyKey="+verifyRequest.getVerifyKey();

		Map<String, String> emailMap=new HashMap<String, String>();
		emailMap.put("name", user.getUserName());
		emailMap.put("verifyCode", verifyRequest.getVerifyCode());
		emailMap.put("link", link);
		emailMap.put("unsubscribe", unsubscribe + email + "&hid=" + user.getHash());
		
		String content= TemplateFile.render("VerifyOtp.html", emailMap);
		emailService.sendAsync(email, "[Gaka] Xác nhận đăng ký", content);
	}

	public void sendResetPassword(UserVO user ,VerifyRequest verifyRequest) throws EmailException {
    	Long id = user.getId();
		log.debug("Sending Verify Email user", id);
		Email emailRequest = new Email();
		emailRequest.setSubject("Forgot password");
		String email = user.getEmail();
		emailRequest.setReceiver(email);
		//send email
		String link = webUrl+"/#/verify-email?email="+email+"&verifyKey="+verifyRequest.getVerifyKey();

		Map<String, String> emailMap=new HashMap<String, String>();
		emailMap.put("name", user.getUserName());
		emailMap.put("link", link);
		emailMap.put("unsubscribe", unsubscribe + email + "&hid=" + user.getHash());
		
		String content= TemplateFile.render("ResetPassword.html", emailMap);
		emailService.sendAsync(email, "[Gaka] Xác nhận quên mật khẩu", content);
	}
		
	private void sendVerifyCode(UserChangeModel userChange) throws EmailException, SmsException {
    	UserVO user = userChange.getUser();
    	String phone = user.getPhone();		
    	String code = user.getVerifyCode();
    	log.debug("Sending Verify code SMS user id {} in {}", phone);
    	String message = "[Gaka] mã xác nhận là: " + code;
		smsService.send(phone,message);
	}
	
	
}
