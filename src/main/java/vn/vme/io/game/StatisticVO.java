package vn.vme.io.game;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticVO implements Serializable{
	
	
	private Integer value;
	
	private String name;
	
	public StatisticVO() {
	}
	
	public String toString() {
		return "[value=" + this.value + "],[name=" + this.name + "]";
	}
}
