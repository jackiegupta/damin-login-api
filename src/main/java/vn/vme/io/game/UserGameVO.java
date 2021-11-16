package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.user.UserVO;

@Getter
@Setter
public class UserGameVO {
	private Long id;
	protected UserVO user;
	private GameVO game;
	private String status;
	public UserGameVO() {
	}

	public UserGameVO(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] game [" + game.getId() + "] status [" + status +"]";
	}
}
