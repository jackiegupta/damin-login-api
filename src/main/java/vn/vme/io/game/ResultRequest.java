package vn.vme.io.game;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultRequest {

	@ApiModelProperty(position = 1)
	private Long id;
	@ApiModelProperty(position = 2)
	private Integer tourId;

	private int position;
	
	private boolean reply;

	protected String status;

	public String toString() {
		return "[tourId=" + this.tourId + "]";
	}

	public ResultRequest() {
	}

}
