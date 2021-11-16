package vn.vme.io.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Data
public class SignInRequest {

	@NotNull
	@NotEmpty
	@ApiModelProperty(notes = "example: dev01.vn@gmail.com, user01, 0947897654", required = true, position = 1)
	private String userName;

	@NotNull
	@NotEmpty
	@ApiModelProperty(notes = "example: Pass!234", required = true, position = 2)
	private String password;

	public SignInRequest() {
	}

	public SignInRequest(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
}
