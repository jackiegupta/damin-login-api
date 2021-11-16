package vn.vme.io.rest360;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import vn.vme.entity.User;

@Getter
@Setter
public class User360 implements Serializable{

	
	private Long id;
	@JsonProperty("user_id")
	private Long userId;
	private String username;
	private String email;
	private String pswd;
	private String msisdn;
	private String fullname;
	private Integer level;
	private Integer vip;
	private Integer exp;
	private Integer gao;
	private Integer gaoKhoa;
	@JsonProperty("social_type")
	private String socialType;
	@JsonProperty("social_id")
	private String socialId;
	private String otp;
	private Date createDate;
	private Date updateDate;
	private Date birthday;
	@JsonProperty("request_id")
	private String requestId;
	
	public User360() {
	}

	public User360(User user) {
		this.username = user.getUserName();
		this.email = "";
		this.msisdn = "";
		this.fullname = (user.getFullName()==null)?"":user.getFullName();
		this.pswd = user.getPassword();
		this.socialType = "OTHER";
		this.socialId = "";
	}

	public User360(int errorCode) {
		this.id = (long) errorCode;
	}

	public String toString() {
		return username + "," + email;
	}
}
