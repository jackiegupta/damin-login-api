package vn.vme.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.game.UserGameRequest;
import vn.vme.io.game.UserGameVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "userGame")
@NamedQuery(name = "UserGame.findAll", query = "SELECT c FROM UserGame c")
@Getter
@Setter
public class UserGame implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "game_id")
	private Game game;

	private Long id360;
	
	private Integer amount;
	
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public UserGame() {
	}

	public UserGame(UserGameRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public UserGame(Long userId, Integer gameId) {
		this.user = new User(userId);
		this.game = new Game(gameId);
	}

	public UserGameVO getVO() {
		UserGameVO response = new UserGameVO();
		BeanUtils.copyProperties(this, response);
		response.setUser(user.getVO());
		response.setGame(game.getVO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
