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
import vn.vme.io.game.PoolTaskRequest;
import vn.vme.io.game.PoolTaskVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "poolTask")
@NamedQuery(name = "PoolTask.findAll", query = "SELECT c FROM PoolTask c")
@Getter
@Setter
public class PoolTask implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "pool_id")
	private Pool pool;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "task_id")
	private Task task;

	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public PoolTask() {
	}

	public PoolTask(PoolTaskRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public PoolTask(Integer poolId, Integer taskId) {
		this.pool = new Pool(poolId);
		this.task = new Task(taskId);
	}

	public PoolTaskVO getVO() {
		PoolTaskVO response = new PoolTaskVO();
		BeanUtils.copyProperties(this, response);
		response.setPool(pool.getVO());
		response.setTask(task.getVO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
