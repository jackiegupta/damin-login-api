package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.DateUtils;
import vn.vme.common.Utils;

@Getter
@Setter
public class UserItemRequest {
	private Long id;
	protected Long userId;
	private Long itemId;
	private String status;
	public UserItemRequest() {
	}

	public UserItemRequest(Long id) {
		this.id = id;
	}

	public UserItemRequest(String name, Long userId, Long itemId, String status, String fromDate, String toDate) {
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
		this.itemId = itemId;
		this.status = status;
		//this.fromDate = fromDate;
		//this.toDate = toDate;
	}
	public String toString() {
		return "id ["+ id + "] item [" + userId + "] status [" + status +"]";
	}
}
