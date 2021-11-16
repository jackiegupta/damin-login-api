package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVO implements Serializable{
	
	private Long id;
	private String name;
	private String title;
	private Long price;
	private String currency;
	private String photo;
	private Integer gameId;
	private String status;	
	private Long createId;
	private Date createDate;
	private Date updateDate;
	
	public ItemVO() {
	}
	public ItemVO(Long id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[name=" + this.name + "]";
	}
}
