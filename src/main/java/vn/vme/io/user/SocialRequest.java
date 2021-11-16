package vn.vme.io.user;

import lombok.Getter;
import lombok.Setter;

/**
 * @MTech Team on 2016-10-15.
 */
@Getter
@Setter
public class SocialRequest{

	protected String id;
	private String authToken;
	private String name;
	private String idToken;
	private String provider;
	protected String email;
	private String fullName ;
	private String photo;
	private String photoUrl;
	private String deviceId;
	
	public String toString(){
		return "User token [" + id + ", " + provider + " " + email + "]";
	}

	public SocialRequest() {
	}
}
