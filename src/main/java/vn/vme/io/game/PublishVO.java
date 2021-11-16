package vn.vme.io.game;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishVO implements Serializable{
	
	private Integer id;
	
	private String name;
	
	private Integer position;
	
	private String content;
	
	private String photo;
	
	private String status;	
	
	public PublishVO() {
	}
	public PublishVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[name=" + this.name + "]";
	}
}
