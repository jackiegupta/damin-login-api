package vn.vme.io.game;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import vn.vme.entity.Pool;

@Getter
@Setter
public class GiftcodeRequest implements Serializable{

	private Long id;
	
	private String code;
	
	private Integer poolId;
	
	private Integer eventId;
	
	private Integer gameId;
	
	private String type;
	
	private Long userId =0L;
	
	private String photo;
	
	private MultipartFile file;
	
	private String status;	
	private Long createId;
	
	public GiftcodeRequest() {

	}

	public GiftcodeRequest(String key, Integer poolId, Integer eventId, Integer gameId, String status, Long userId) {
		this.code = key;
		this.poolId = poolId;
		this.eventId = eventId;
		this.gameId = gameId;
		this.status = status;
		this.userId = userId;
	}
	public GiftcodeRequest(String key, Integer eventId,Long userId, String status) {
		this.code = key;
		this.eventId = eventId;
		this.userId = userId;
		this.status = status;
	}

	public GiftcodeRequest(Integer poolId, String type) {
		this.poolId = poolId;
		this.type = type;
	}

	public GiftcodeRequest(Pool pool) {
		this.poolId = pool.getId();
		this.type = pool.getType();
		/*
		if(pool.getEvent()!=null) {
			this.eventId = pool.getEvent().getId();
		}
		if(pool.getGame()!=null) {
			this.gameId = pool.getGame().getId();
		}
		*/
	}

	public String toString() {
		return id + "," + eventId;
	}
}
