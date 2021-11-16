package vn.vme.io.user;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDO implements Serializable{
	protected Long id;
	protected String userName;
	private int loginCount;
	protected String photo;
	protected String level;
	protected String vip;
	protected String fullName;
	protected String city;
	private String type;	
	
	public UserDO() {
	}
	
	public UserDO(int loginCount) {
		this.loginCount = loginCount;
	}

	public String toString() {
		return userName;
	}	
}
