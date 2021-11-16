package vn.vme.io.game;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.DateUtils;
import vn.vme.common.Utils;

@Getter
@Setter
public class UserTaskRequest {
	private Long id;
	protected Long userId;
	protected Integer poolId;
	private Integer eventId;
	private Integer taskId;
	private String status;
	private String updateDate;
	
	public UserTaskRequest() {
	}

	public UserTaskRequest(Long id) {
		this.id = id;
	}
	public UserTaskRequest(String name, Long userId,Integer poolId, Integer eventId, Integer taskId,  String status, String fromDate, String toDate) {
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
		this.taskId = taskId;
		this.status = status;
		this.poolId = poolId;
		this.eventId = eventId;
		//this.fromDate = fromDate;
		//this.toDate = toDate;
	}
	
	public String toString() {
		return "userId ["+ userId + "] task [" + taskId + "] status [" + status +"]";
	}
}
