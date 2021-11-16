package vn.vme.io.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Data
public class UserRequest {

	@ApiModelProperty(position = 1)
	protected Long id;
	
	private String userName;
	
	@ApiModelProperty(position = 2)
	@Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$", message = "Invalid email")
	@Size(max = 50)
	protected String email;
	
	protected String phone;
	@Size(max = 10, message = "name maximum 10 characters")
	
	private String password;
	private String gender;
	private String address;
	private String fullName ;
	private String district;
	private String city;
	private String country;
	private String postCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	protected Date birthDate;
	private String identity;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	protected Date identityDate;
	private String identityPlace;
	private String photo;
	private String type;
	private boolean notify;	
	private Integer money;
	private Integer rice;
	private Integer seed;
	private Integer point;
	private Integer exp;
	private String level;
	private String friendCode;
	private String vip;
	protected String status;
	protected Date updateDate;
	protected List<RoleVO> roles;

	

	@JsonIgnore
	public void addRole(int ordinal) {
		if (roles == null) {
			roles = new ArrayList<RoleVO>();
		}
		roles.add(new RoleVO(ordinal));
	}

	public UserRequest() {
	}

	public UserRequest(String userName) {
		this.userName = userName;
	}

	
	public UserRequest(SocialRequest socialRequest) {
		BeanUtils.copyProperties(socialRequest, this);
	}

	public UserRequest(VerifyRequest request) {
		this.userName = request.getUserName();		
	}

	public UserRequest(String key, String level, String status) {
		this.userName = key;		
		this.level = level;
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserRequest [userName=" + userName + "]";
	}

}
