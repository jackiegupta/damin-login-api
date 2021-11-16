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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.game.PoolRequest;
import vn.vme.io.game.PoolVO;
import vn.vme.io.game.TaskVO;

@Entity
@Table(schema = "gate_schema", name = "pool")
@NamedQuery(name = "Pool.findAll", query = "SELECT c FROM Pool c")
@Getter
@Setter
public class Pool implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String title;
	private String content;
	private Integer count;
	private Integer size;
	private String type; //ONE, MULTI, ALL
	private String scope; //EVENT, GAME
	private String photo;
	private String status;
	//@OneToOne(cascade= CascadeType.DETACH)
	//@JoinColumn(name = "event_id", nullable = true)
	//private Event event;
	private Integer rice;
	private Integer seed;
	private Integer expPoint;
	private String items;// format ID-Quantity: 1-3,2-1
	
	//@OneToOne(cascade= CascadeType.DETACH)
	//@JoinColumn(name = "game_id", nullable = true )
	//private Game game;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	//@OneToMany(cascade= CascadeType.DETACH)
	//@JoinColumn(name = "pool_id")
	//private Set<PoolTask> poolTasks;
	

	public Pool() {
	}

	public Pool(Integer poolId) {
		this.id = poolId;
	}

	public Pool(PoolVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Pool(PoolRequest request) {
		BeanUtils.copyProperties(request, this);
		
	}
	
	public PoolVO getVO() {
		PoolVO response = new PoolVO();
		BeanUtils.copyProperties(this, response);
		/*
		if (event != null) {
			//response.setEvent(event.getVO());
		}
		if (game != null) {
			response.setGame(game.getVO());
		}
		
		if (poolTasks != null) {
			List<TaskVO> taskList = new ArrayList<TaskVO>();
			for (PoolTask poolTask : poolTasks) {
				TaskVO taskVO = new TaskVO();
				BeanUtils.copyProperties(poolTask.getTask(), taskVO);
				taskList.add(taskVO );
			}
			response.setTasks(taskList);
		}*/
		return response;
	}


	@Override
	public String toString() {
		return "Pool [id=" + id + ", count=" + count + "]";
	}


	
	
	
}
