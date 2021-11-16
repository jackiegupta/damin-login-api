package vn.vme.io.user;

import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Data
public class ProfileRequest {

	@ApiModelProperty(position = 1)
	
	@Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$", message = "Invalid email")
	@Size(max = 50)
	protected String email;
	
	protected String phone;
	@Size(max = 10, message = "name maximum 10 characters")
	
	private String gender;
	private String address;
	private String fullName ;
	private String district;
	private String firebaseId;
	private String deviceId;
	private String city;
	private String country;
	private String postCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	protected Date birthDate;
	private String identity;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	protected Date identityDate;
	private String identityPlace;
	private String photo;
	private String type;
	private boolean notify;	


	public ProfileRequest() {
	}

	public ProfileRequest(SocialRequest socialRequest) {
		BeanUtils.copyProperties(socialRequest, this);
	}

	

	@Override
	public String toString() {
		return "UserRequest [email=" + email + ", phone=" + phone 
				+ ", gender=" + gender + ", address=" + address + ", fullName =" + fullName 
				+ ", district=" + district + ", city=" + city + ", country=" + country + ", postCode=" + postCode
				+ ", birthDate=" + birthDate + ", identity=" + identity + ", identityDate=" + identityDate + ", photo=" + photo
			    + "]";
	}

}
