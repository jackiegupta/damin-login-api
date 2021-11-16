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
public class PoolVO implements Serializable{
	
	private Integer id;
	
	private String name;
	private String title;
	private String content;
	
	private String photo;
	
	private Integer count;
	
	private Integer size;
	
	private String type; //ONE, MULTI, ALL
	
	private String scope;
	private Integer rice;
	private Integer seed;
	private Integer expPoint;
	private String items;// format ID-Quantity: 1-3,2-1
	
	private String status;
	private EventVO event;
	
	private GameVO game;
	
	private Long createId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	protected Date createDate;
	
	protected List<TaskVO> tasks;
	
	public PoolVO() {
	}
	public PoolVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[count=" + this.count + "]";
	}
}
