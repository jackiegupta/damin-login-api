package vn.vme.io.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @MTech Team on 2016-10-15.
 */
@Getter
@Setter
public class SocialVO implements Serializable{

	protected String id;
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("refresh_token")
	private String refreshToken;
	private String authorities;
	@JsonProperty("user_name")
	private String userName;
	@JsonProperty("expires_in")
	private String expiresIn;
	@JsonProperty("user")
	protected UserVO userVO;
	
	public String toString(){
		return "User token [" + id + ", " + userName + "]";
	}

	public SocialVO() {
	}

	public SocialVO(UserVO userVO) {
		this.id = String.valueOf(userVO.getId());
		this.userName = userVO.getUserName();
		this.userVO = userVO;
	}
}
