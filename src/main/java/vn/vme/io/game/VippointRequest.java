package vn.vme.io.game;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VippointRequest implements Serializable{

	private Integer id;
	private String name;
	private String code;
	private String value;
	private String status;	
	
	public VippointRequest() {

	}

	public VippointRequest(String code, String status) {
		this.name = code;
		this.status = status;
	}

	public String toString() {
		return id + "," + name;
	}
}
