package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventPoolRequest {
	private Long id;
	protected EventVO event;
	private PoolVO pool;
	private String status;
	public EventPoolRequest() {
	}

	public EventPoolRequest(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] pool [" + pool.getId() + "] status [" + status +"]";
	}
}
