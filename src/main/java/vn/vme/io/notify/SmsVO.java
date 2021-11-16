package vn.vme.io.notify;

import java.io.Serializable;

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
public class SmsVO implements Serializable{

	@ApiModelProperty(position = 1, example = "0")
	private Long id;
	@NotNull
    @NotEmpty
    @ApiModelProperty(position = 2, example = "Your code confirmation is 123456")
	private String message;
	@NotNull
    @NotEmpty
    @ApiModelProperty(position = 3, example = "0981068900")
	private String phone;
	
	@ApiModelProperty(position = 9, example = "UNVERIFY")
	private String status;
}
