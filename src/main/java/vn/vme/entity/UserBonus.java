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
import vn.vme.io.game.UserBonusVO;
import vn.vme.io.user.UserFriendRequest;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "userBonus")
@NamedQuery(name = "UserBonus.findAll", query = "SELECT c FROM UserBonus c")
@Getter
@Setter
public class UserBonus implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "bonus_id")
	private Bonus bonus;

	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public UserBonus() {
	}

	public UserBonus(UserFriendRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public UserBonus(Long userId, Integer bonusId) {
		this.user = new User(userId);
		this.bonus = new Bonus(bonusId);
	}

	public UserBonusVO getVO() {
		UserBonusVO response = new UserBonusVO();
		BeanUtils.copyProperties(this, response);
		response.setUser(user.getVO());
		response.setBonus(bonus.getVO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
