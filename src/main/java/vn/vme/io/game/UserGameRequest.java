package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.DateUtils;
import vn.vme.common.Utils;

@Getter
@Setter
public class UserGameRequest {
	private Long id;
	protected Long userId;
	private Integer gameId;
	private Integer amount;
	private String status;
	public UserGameRequest() {
	}

	public UserGameRequest(Long id) {
		this.id = id;
	}

	public UserGameRequest(Long userId, Integer gameId, String status) {
		this.userId = userId;
		this.gameId = gameId;
		this.status = status;
	}
	public UserGameRequest(String name, Long userId, Integer gameId, String status, String fromDate, String toDate) {
		if (!fromDate.contains("undefined--") && Utils.isNotEmpty(fromDate) && DateUtils.toDateYYYYMMDD(fromDate) != null) {
			fromDate = fromDate + " 00:00:00";
		}else {
			fromDate = "";
		}
		if (!toDate.contains("undefined--") && Utils.isNotEmpty(toDate) && DateUtils.toDateYYYYMMDD(toDate) != null) {
			toDate = toDate + " 00:00:00";
		}else {
			toDate = "";
		}
		this.userId = userId;
		this.gameId = gameId;
		this.status = status;
		//this.fromDate = fromDate;
		//this.toDate = toDate;
	}
	public String toString() {
		return "id ["+ id + "] game [" + userId + "] status [" + status +"]";
	}
}
