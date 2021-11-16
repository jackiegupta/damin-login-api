package vn.vme.io.notify;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class EmailMultiWFileRequest {

    private Long id;
    @NotNull
    private String subject;
    @NotNull
    private String content;
    private String sender;
    @NotNull
//    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$", message = "Invalid email")
    private String[] receivers;
    private String[] cc;
    private String[] bcc;

    private String status;

    @ApiModelProperty(position = 4, example = "icom.vn")
    private String domainName;

    private MultipartFile[] attachFiles;
    @JsonProperty
    private String isHtml;

    @Override
    public String toString() {
        return "EmailMultiWFileRequest{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", content='" + '\'' +
                ", sender='" + sender + '\'' +
                ", receivers=" + Arrays.toString(receivers) +
                ", cc=" + Arrays.toString(cc) +
                ", bcc=" + Arrays.toString(bcc) +
                ", status='" + status + '\'' +
                ", domainName='" + domainName + '\'' +
                ", attachFiles=" + Arrays.toString(attachFiles) +
                ", isHtml=" + isHtml +
                '}';
    }
}
