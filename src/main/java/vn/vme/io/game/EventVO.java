package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventVO implements Serializable{
	
	private Integer id;
	
	private String name;
	private String title;
	
	private Integer giftCount;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	private Integer position;
	private String photo;
	private String content;
	private String status;	
	private Long createId;
	
	protected Date createDate;
	
	protected List<PoolVO> pools;
	
	protected List<GameVO> games;
	
	public EventVO() {
	}
	public EventVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[name=" + this.name + "]";
	}
}
