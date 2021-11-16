package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.DateUtils;
import vn.vme.common.Utils;

@Getter
@Setter
public class UserGiftcodeRequest {
	private Long id;
	protected Long userId;
	
	private Long giftcodeId;
	
	private String status;
	
	
	private String code;
	
	private Integer gameId;
	
	private Integer eventId;
	
	private Integer serverId;
	
	private String actorName;
	
	public UserGiftcodeRequest() {
	}


	public UserGiftcodeRequest(String code,Long giftcodeId, Integer gameId,Integer eventId, Long userId, Integer serverId, String actorName, String status) {
		this.code = code;
		this.gameId = gameId;
		this.giftcodeId = giftcodeId;
		this.eventId = eventId;
		this.userId = userId;
		this.serverId = serverId;
		this.actorName = actorName;
		this.status = status;
	}
	public UserGiftcodeRequest(Long id) {
		this.id = id;
	}
	public UserGiftcodeRequest(String name, Long userId, Long giftcodeId, String status, String fromDate, String toDate) {
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
		this.giftcodeId = giftcodeId;
		this.status = status;
		//this.fromDate = fromDate;
		//this.toDate = toDate;
	}
	
	public String toString() {
		return "userId ["+ userId + "] giftcode [" + giftcodeId + "] status [" + status +"]";
	}
}
