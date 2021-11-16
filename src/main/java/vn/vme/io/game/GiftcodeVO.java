package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.user.UserDO;

@Getter
@Setter
public class GiftcodeVO implements Serializable{
	
	private Long id;
	
	private String code;
	
	private String type; //ONE, MULTI, ALL
	
	private PoolVO pool;
	
	private EventVO event;
	
	private GameVO game;
	
	private UserDO user;
	
	private String status;	
	
	private Long createId;
	
	protected Date createDate;
	
	public GiftcodeVO() {
	}
	public GiftcodeVO(Long id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[pool=" + this.pool + "]";
	}
}
