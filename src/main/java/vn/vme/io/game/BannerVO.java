package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerVO implements Serializable{
	
	private Integer id;
	private String name;
	private String title;
	private String content;
	private String photo;
	private String link;
	private String status;
	private GameVO game;
	private Integer position;
	private Long createId;
	private Date createDate;
	private Date updateDate;
	
	public BannerVO() {
	}
	public BannerVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[name=" + this.name + "]";
	}
}
