package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.user.UserVO;

@Getter
@Setter
public class UserBonusVO {
	private Long id;
	protected UserVO user;
	private BonusVO bonus;
	private String status;
	public UserBonusVO() {
	}

	public UserBonusVO(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] bonus [" + bonus.getId() + "] status [" + status +"]";
	}
}
