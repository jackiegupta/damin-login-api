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
import vn.vme.io.game.EventPoolRequest;
import vn.vme.io.game.EventPoolVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "eventPool")
@NamedQuery(name = "EventPool.findAll", query = "SELECT c FROM EventPool c")
@Getter
@Setter
public class EventPool implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "event_id")
	private Event event;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "pool_id")
	private Pool pool;

	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public EventPool() {
	}

	public EventPool(EventPoolRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public EventPool(Integer eventId, Integer poolId) {
		this.event = new Event(eventId);
		this.pool = new Pool(poolId);
		this.status = Status.ACTIVE.name();
		this.createDate = new Date();
		this.updateDate = new Date();
	}

	public EventPoolVO getVO() {
		EventPoolVO response = new EventPoolVO();
		BeanUtils.copyProperties(this, response);
		response.setEvent(event.getVO());
		response.setPool(pool.getVO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
