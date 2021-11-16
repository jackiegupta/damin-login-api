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
import vn.vme.io.game.UserTaskVO;
import vn.vme.io.user.UserFriendRequest;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "userTask")
@NamedQuery(name = "UserTask.findAll", query = "SELECT c FROM UserTask c")
@Getter
@Setter
public class UserTask implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "task_id")
	private Task task;

	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public UserTask() {
	}

	public UserTask(UserFriendRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public UserTask(Long userId, Integer taskId) {
		this.user = new User(userId);
		this.task = new Task(taskId);
	}

	public UserTaskVO getVO() {
		UserTaskVO response = new UserTaskVO();
		BeanUtils.copyProperties(this, response);
		response.setUser(user.getVO());
		response.setTask(task.getVO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
