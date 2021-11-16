package vn.vme.io.game;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistic implements Serializable {

	private long value;

	private String name;

	public Statistic() {
	}

	public Statistic(String name, long value) {
		this.name = name;
		this.value = value;
	}

	public String toString() {
		return "[value=" + this.value + "],[name=" + this.name + "]";
	}
}
