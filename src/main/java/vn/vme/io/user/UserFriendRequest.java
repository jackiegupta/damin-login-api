package vn.vme.io.user;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Data
public class UserFriendRequest {

	@ApiModelProperty(position = 1)
	protected Long id;
	@ApiModelProperty(position = 2)
	private Long userId;
	private Long friendId;
	
	protected String status;
	
	protected Date updateDate;
	
	public String toString() {
		return userId + "," + friendId;
	}

	public UserFriendRequest() {
	}

}
