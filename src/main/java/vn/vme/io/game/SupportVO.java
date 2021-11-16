package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportVO implements Serializable{
	
	private Integer id;
	
	private String name;
	private String title;
	private Integer position;
	private String photo;
	private String content;
	private String status;	
	private Long createId;
	
	protected Date createDate;
	
	
	public SupportVO() {
	}
	public SupportVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[name=" + this.name + "]";
	}
}
