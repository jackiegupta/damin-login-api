package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBonusRequest {
	private Long id;
	protected Long userId;
	private Integer bonusId;
	private String status;
	public UserBonusRequest() {
	}

	public UserBonusRequest(Long id) {
		this.id = id;
	}
	public UserBonusRequest(Long userId,Integer bonusId) {
		this.userId = userId;
		this.bonusId = bonusId;
	}
	
	public String toString() {
		return "userId ["+ userId + "] bonus [" + bonusId + "] status [" + status +"]";
	}
}
