package vn.vme.io.notify;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVO implements Serializable{

	@ApiModelProperty(position =1)
	private Long id;
	@ApiModelProperty(position =2)
    private String receiver;
    private String sender;
    private String subject;
    private String content;
    private String status;
    private String createDate;

    public EmailVO() {
	}
   
}
