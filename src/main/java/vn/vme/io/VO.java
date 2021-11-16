package vn.vme.io;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VO implements Serializable{

	public String key;

	public String value;

	public VO() {
	}

	public VO(String key,String value) {
		this.key = key;
		this.value = value;
	}

	public VO(String key) {
		this.key = key;
	}
	
}