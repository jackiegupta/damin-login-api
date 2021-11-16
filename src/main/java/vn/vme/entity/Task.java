package vn.vme.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.game.TaskRequest;
import vn.vme.io.game.TaskVO;

@Entity
@Table(schema = "gate_schema", name = "task")
@NamedQuery(name = "Task.findAll", query = "SELECT c FROM Task c")
@Getter
@Setter
public class Task implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String code;
	private Integer rice;
	private Integer seed;
	private Integer expPoint;
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Task() {
	}

	public Task(TaskVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Task(TaskRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Task(Integer taskId) {
		this.id = taskId;
	}

	public TaskVO getVO() {
		TaskVO response = new TaskVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
