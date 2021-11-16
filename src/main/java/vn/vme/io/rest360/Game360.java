package vn.vme.io.rest360;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import vn.vme.entity.User;

@Getter
@Setter
public class Game360 implements Serializable{

	@JsonProperty("game_id")
	private Integer gameId;
	private Integer amount;
	@JsonProperty("user_id")
	private Long userId;
	
	@JsonProperty("request_id")
	private String requestId;
	
	public Game360() {
	}

	public Game360(User user) {
	}

	public Game360(int gameId, Long userId, Integer amount) {
		this.gameId = gameId;
		this.userId = userId;
		this.amount = amount;
	}

	public String toString() {
		return gameId + "," + userId;
	}
}
