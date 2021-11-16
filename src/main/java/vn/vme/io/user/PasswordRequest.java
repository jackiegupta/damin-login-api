package vn.vme.io.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequest {

	private String email;
  
    private String userName;
    
    private String password;
    
    private String currentPassword;
    
    private String verifyCode;
    
    private String verifyKey;
    
    public PasswordRequest() {
    	
    }
    		
    public PasswordRequest(String userName, String token) {
		this.userName = userName;
		this.password = token;
	}
   
}
