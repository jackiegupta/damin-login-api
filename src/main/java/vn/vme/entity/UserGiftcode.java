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
import vn.vme.io.game.UserGiftcodeVO;
import vn.vme.io.user.UserFriendRequest;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "userGiftcode")
@NamedQuery(name = "UserGiftcode.findAll", query = "SELECT c FROM UserGiftcode c")
@Getter
@Setter
public class UserGiftcode implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "giftcode_id")
	private Giftcode giftcode;

	@OneToOne(cascade= CascadeType.DETACH)
	@JoinColumn(name = "game_id")
	private Game game;
	
	private String status;
	
	private Integer serverId;
	
	private String actorName;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public UserGiftcode() {
	}

	public UserGiftcode(UserFriendRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public UserGiftcode(Long userId, Long giftcodeId) {
		this.user = new User(userId);
		this.giftcode = new Giftcode(giftcodeId);
	}

	public UserGiftcodeVO getVO() {
		UserGiftcodeVO response = new UserGiftcodeVO();
		BeanUtils.copyProperties(this, response);
		response.setUser(user.getVO());
		response.setGiftcode(giftcode.getVO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
