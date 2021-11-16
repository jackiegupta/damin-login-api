package vn.vme.io.notify;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {

	private Long id;
    @NotNull
    private String subject;
    @NotNull
    private String content;
    @NotNull
    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$", message = "Invalid email")
    private String receiver;
    
    private String status;
    
    @ApiModelProperty(position = 4, example = "gaka.vn")
    private String domainName;
   
}
