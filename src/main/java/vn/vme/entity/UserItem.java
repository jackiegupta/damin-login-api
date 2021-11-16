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
import vn.vme.io.game.UserItemRequest;
import vn.vme.io.game.UserItemVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "userItem")
@NamedQuery(name = "UserItem.findAll", query = "SELECT c FROM UserItem c")
@Getter
@Setter
public class UserItem implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "item_id")
	private Item item;

	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public UserItem() {
	}

	public UserItem(UserItemRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public UserItem(Long userId, Long itemId) {
		this.user = new User(userId);
		this.item = new Item(itemId);
	}

	public UserItemVO getVO() {
		UserItemVO response = new UserItemVO();
		BeanUtils.copyProperties(this, response);
		response.setUser(user.getVO());
		response.setItem(item.getVO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
