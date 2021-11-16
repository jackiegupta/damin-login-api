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
public class BonusVO implements Serializable{
	
	private Integer id;
	
	private String name;
	
	private String title;
	
	private Integer levelId;
	
	private Integer rice;
	private Integer seed;
	private Integer expPoint;
	private String items;// format ID-Quantity: 1-3,2-1
	private String photo;
	private String scope;
	
	private String status;
	
	private Long createId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	protected Date createDate;
	
	
	public BonusVO() {
	}
	public BonusVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[name=" + this.name + "]";
	}
}
