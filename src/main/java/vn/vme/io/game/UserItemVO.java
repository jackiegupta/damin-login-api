package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.user.UserVO;

@Getter
@Setter
public class UserItemVO {
	private Long id;
	protected UserVO user;
	private ItemVO item;
	private String status;
	public UserItemVO() {
	}

	public UserItemVO(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] item [" + item.getId() + "] status [" + status +"]";
	}
}
