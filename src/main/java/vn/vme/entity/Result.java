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
import vn.vme.io.game.ResultRequest;
import vn.vme.io.game.ResultVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "result")
@NamedQuery(name = "Result.findAll", query = "SELECT c FROM Result c")
@Getter
@Setter
public class Result implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "tour_id")
	private Tour tour;

	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "user_id")
	private User user;

	private int position;
	
	private boolean win;
		
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	public Result() {
	}

	public Result(ResultRequest request) {
		BeanUtils.copyProperties(request, this);
		this.setTour(new Tour(request.getTourId()));
		
	}

	
	
	public Result(Long id) {
		this.id = id;
	}

	public ResultVO getVO() {
		ResultVO response = new ResultVO();
		BeanUtils.copyProperties(this, response);
		response.setTourId(this.tour.getId());
		return response;
	}

	public String toString() {
		return "[id=" + this.id + "],[position=" + this.position + "],[userId=" + this.user.getId() + "]";
	}
}
