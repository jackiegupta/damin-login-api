package vn.vme.io.user;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleVO implements Serializable{

	private Integer id;

	private String name;

	private String type;

	public RoleVO() {
	}

	public RoleVO(int id) {
		this.id = id;
	}
	
}