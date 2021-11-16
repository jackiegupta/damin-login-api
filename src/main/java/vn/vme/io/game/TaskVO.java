package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskVO implements Serializable{
	
	private Integer id;
	
	private String name;
	private String code;
	
	private Integer rice;
	private Integer seed;
	private Integer expPoint;
	
	private String status;	
	private Long createId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	private Date createDate;
	private Date updateDate;
	
	public TaskVO() {
	}
	public TaskVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[name=" + this.name + "]";
	}
}
