package vn.vme.io.rest360;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Package360 implements Serializable{

	
	private Long id;
	private String packageCode;
	private String packageName;
	private String title;
	private String info;
	private Integer freeDay;
	private Integer price;
	private Integer scoreReg;
	private Integer status;
	
	public Package360() {
	}

	public Package360(int errorCode) {
		this.id = (long) errorCode;
	}

	public String toString() {
		return packageCode + "," + packageName;
	}
}
