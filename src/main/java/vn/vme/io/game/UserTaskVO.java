package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.user.UserVO;

@Getter
@Setter
public class UserTaskVO {
	private Long id;
	protected UserVO user;
	private TaskVO task;
	private String status;
	public UserTaskVO() {
	}

	public UserTaskVO(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] task [" + task.getId() + "] status [" + status +"]";
	}
}
