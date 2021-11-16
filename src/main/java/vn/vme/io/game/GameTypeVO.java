package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameTypeVO implements Serializable{
	
	protected Integer id;
	
	private String name;
	
	private Integer position;
	
	private String content;
	
	private String photo;
	
	private String status;
	
	private Long createId;
	
	protected Date createDate;
	
	public GameTypeVO() {
	}
	public GameTypeVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[name=" + this.name + "]";
	}
}
