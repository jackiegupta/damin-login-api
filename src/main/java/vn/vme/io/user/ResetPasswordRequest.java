package vn.vme.io.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Data
public class ResetPasswordRequest{

    @NotNull
    @NotEmpty
    @Pattern( regexp ="^(?!\\s*$)[0-9\\s]{6}$", message ="Invalid code format")
    private String code;

    @NotNull
    @NotEmpty    
    private String userId;

    @NotNull
    @NotEmpty
    private String password;
   
}
