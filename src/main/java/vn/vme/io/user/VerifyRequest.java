package vn.vme.io.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRequest {

	private String verifyKey;
    private String verifyCode;
    private String userName;
    private String email;
    
    private String phone;
   
    public VerifyRequest() {
		 
	 }
   public VerifyRequest(String userName, String verifyCode) {
		this.userName = userName;
		this.verifyCode = verifyCode;
	}
}
