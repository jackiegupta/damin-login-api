package vn.vme.io.game;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendRequest implements Serializable{

	private Integer id;
	private String name;
	private String code;
	private String value;
	private String status;	
	
	public AttendRequest() {

	}

	public AttendRequest(String code, String status) {
		this.name = code;
		this.status = status;
	}

	public String toString() {
		return id + "," + name;
	}
}
