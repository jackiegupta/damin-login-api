package vn.vme.event;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.user.UserVO;
import vn.vme.io.user.VerifyRequest;

@Getter
@Setter
public class UserChangeModel{
    private String type;
    private String action;
    private UserVO user;
    private VerifyRequest verifyRequest;
    private String correlationId;

    public  UserChangeModel() {
    	
    }

    public  UserChangeModel(String type, String action, UserVO user,VerifyRequest verifyRequest, String correlationId) {
        super();
        this.type   = type;
        this.action = action;
        this.user = user;
        this.verifyRequest = verifyRequest;
        this.correlationId = correlationId;
    }
}