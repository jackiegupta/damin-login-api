package vn.vme.io.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventGameRequest {
	private Long id;
	protected EventVO event;
	private GameVO game;
	private String status;
	public EventGameRequest() {
	}

	public EventGameRequest(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] game [" + game.getId() + "] status [" + status +"]";
	}
}
