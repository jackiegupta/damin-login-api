package vn.vme.io.notify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

	@ApiModelProperty(position = 1)
	private Long id;
	@ApiModelProperty(position = 2)
	private Long userId;

	private String title;
	
	private String content;
	
	private String target;
	
	private boolean seen;

	protected String status;

	public String toString() {
		return "[userId=" + this.userId + "]";
	}

	public CommentRequest() {
	}

}
