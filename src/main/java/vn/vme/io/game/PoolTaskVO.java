package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoolTaskVO {
	private Long id;
	protected PoolVO pool;
	private TaskVO task;
	private String status;
	public PoolTaskVO() {
	}

	public PoolTaskVO(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] task [" + task.getId() + "] status [" + status +"]";
	}
}
