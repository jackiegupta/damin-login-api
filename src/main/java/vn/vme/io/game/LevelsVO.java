package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelsVO implements Serializable{
	
	private Integer id;
	private String name;
	private String code;
	private String value;
	private String status;	
	private Long createId;
	private Date createDate;
	private Date updateDate;
	
	public LevelsVO() {
	}
	public LevelsVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[name=" + this.name + "]";
	}
}
