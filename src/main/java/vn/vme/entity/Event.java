package vn.vme.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.game.EventRequest;
import vn.vme.io.game.EventVO;
import vn.vme.io.game.GameVO;
import vn.vme.io.game.PoolVO;

@Entity
@Table(schema = "gate_schema", name = "event")
@NamedQuery(name = "Event.findAll", query = "SELECT c FROM Event c")
@Getter
@Setter
public class Event implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String title;
	
	//@OneToOne(cascade= CascadeType.DETACH)
	//@JoinColumn(name = "game_id")
	//private Game game;
	
	private Integer giftCount;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	private Integer position;
	private String photo;
	private String content;
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@OneToMany(cascade= CascadeType.DETACH)
	@JoinColumn(name = "event_id")
	private Set<EventPool> eventPools;
	
	@OneToMany(cascade= CascadeType.DETACH)
	@JoinColumn(name = "event_id")
	private Set<EventGame> eventGames;
	
	

	public Event() {
	}

	public Event(EventVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Event(EventRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Event(Integer eventId) {
		this.id = eventId;
	}

	public EventVO getVO() {
		EventVO response = new EventVO();
		BeanUtils.copyProperties(this, response);
		if (eventGames != null) {
			List<GameVO> gameList = new ArrayList<GameVO>();
			for (EventGame eventGame : eventGames) {
				GameVO gameVO = new GameVO();
				BeanUtils.copyProperties(eventGame.getGame(), gameVO);
				gameList.add(gameVO );
			}
			response.setGames(gameList);
		}
		if (eventPools != null) {
			List<PoolVO> poolList = new ArrayList<PoolVO>();
			for (EventPool eventPool : eventPools) {
				PoolVO poolVO = new PoolVO();
				BeanUtils.copyProperties(eventPool.getPool(), poolVO);
				poolList.add(poolVO );
			}
			response.setPools(poolList);
		}
		return response;
	}


	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
