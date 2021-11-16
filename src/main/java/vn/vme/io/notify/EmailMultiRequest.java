package vn.vme.io.notify;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailMultiRequest {

    private Long id;
    @NotNull
    private String subject;
    @NotNull
    private String content;
    @NotNull
    private String sender;
    //    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$", message = "Invalid email")
    private String[] receivers;
    private String[] cc;
    private String[] bcc;

    private String status;

    @ApiModelProperty(position = 4, example = "icom.vn")
    private String domainName;

    //    MultipartFile[] attachFiles;
    private String[] filePaths;
    @JsonProperty
    private int isHtml;

    @Override
    public String toString() {
        return "EmailMultiRequest{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", content='" + "{Content}" + '\'' +
                ", sender='" + sender + '\'' +
                ", receivers=" + Arrays.toString(receivers) +
                ", cc=" + Arrays.toString(cc) +
                ", bcc=" + Arrays.toString(bcc) +
                ", status='" + status + '\'' +
                ", domainName='" + domainName + '\'' +
                ", filePaths=" + Arrays.toString(filePaths) +
                ", isHtml=" + isHtml +
                '}';
    }
}
