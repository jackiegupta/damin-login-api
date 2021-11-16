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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.game.GiftcodeRequest;
import vn.vme.io.game.GiftcodeVO;

@Entity
@Table(schema = "gate_schema", name = "giftcode")
@NamedQuery(name = "Giftcode.findAll", query = "SELECT c FROM Giftcode c")
@Getter
@Setter
public class Giftcode implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String code;
	
	@OneToOne(cascade= CascadeType.DETACH)
	@JoinColumn(name = "pool_id")
	private Pool pool;
	
	@OneToOne(cascade= CascadeType.DETACH)
	@JoinColumn(name = "event_id")
	private Event event;
	
	
	@OneToOne(cascade= CascadeType.DETACH)
	@JoinColumn(name = "game_id")
	private Game game;
	
	@ManyToOne(cascade= CascadeType.DETACH)
	@JoinColumn(name = "user_id")
	private User user;
	private String type; //SINGLE, MULTI, ALL
	
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Giftcode() {
	}

	public Giftcode(Long id) {
		this.id = id;
	}

	public Giftcode(GiftcodeVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Giftcode(GiftcodeRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public GiftcodeVO getVO() {
		GiftcodeVO response = new GiftcodeVO();
		BeanUtils.copyProperties(this, response);
		if (pool != null) {
			response.setPool(pool.getVO());
		}
		if (event != null) {
			//response.setEvent(event.getVO());
		}
		if (user != null) {
			response.setUser(user.getVO().getDO());
		}
		return response;
	}


	@Override
	public String toString() {
		return "Giftcode [id=" + id + ", code=" + code + "]";
	}


	
	
	
}
