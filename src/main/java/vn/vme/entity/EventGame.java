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
import vn.vme.common.JConstants.Status;
import vn.vme.io.game.EventGameRequest;
import vn.vme.io.game.EventGameVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "eventGame")
@NamedQuery(name = "EventGame.findAll", query = "SELECT c FROM EventGame c")
@Getter
@Setter
public class EventGame implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "event_id")
	private Event event;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "game_id")
	private Game game;

	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public EventGame() {
	}

	public EventGame(EventGameRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public EventGame(Integer eventId, Integer gameId) {
		this.event = new Event(eventId);
		this.game = new Game(gameId);
		this.status = Status.ACTIVE.name();
		this.createDate = new Date();
		this.updateDate = new Date();
	}

	public EventGameVO getVO() {
		EventGameVO response = new EventGameVO();
		BeanUtils.copyProperties(this, response);
		response.setEvent(event.getVO());
		response.setGame(game.getVO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
