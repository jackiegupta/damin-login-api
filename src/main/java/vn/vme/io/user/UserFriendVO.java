package vn.vme.io.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFriendVO {
	private Long id;
	private Long userId;
	private String status;
	protected UserDO friend;
	public UserFriendVO() {
	}

	public UserFriendVO(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] friend [" + friend.getId() + "] status [" + status +"]";
	}
}
