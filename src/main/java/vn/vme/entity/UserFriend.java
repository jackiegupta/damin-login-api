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
import vn.vme.io.user.UserFriendRequest;
import vn.vme.io.user.UserFriendVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "userFriend")
@NamedQuery(name = "UserFriend.findAll", query = "SELECT c FROM UserFriend c")
@Getter
@Setter
public class UserFriend implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "friend_id")
	private User friend;

	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public UserFriend() {
	}

	public UserFriend(UserFriendRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public UserFriend(Long userId, Long friendId) {
		this.user = new User(userId);
		this.friend = new User(friendId);
	}

	public UserFriendVO getVO() {
		UserFriendVO response = new UserFriendVO();
		BeanUtils.copyProperties(this, response);
		response.setUserId(user.getId());
		response.setFriend(friend.getVO().getDO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
