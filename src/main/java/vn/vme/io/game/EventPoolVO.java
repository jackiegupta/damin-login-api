package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventPoolVO {
	private Long id;
	protected EventVO event;
	private PoolVO pool;
	private String status;
	public EventPoolVO() {
	}

	public EventPoolVO(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] pool [" + pool.getId() + "] status [" + status +"]";
	}
}
