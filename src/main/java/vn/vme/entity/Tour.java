package vn.vme.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import vn.vme.common.JConstants.TourStatus;
import vn.vme.io.game.ResultVO;
import vn.vme.io.game.TourRequest;
import vn.vme.io.game.TourVO;
import vn.vme.io.user.UserDO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema="gate_schema", name="tour")
@NamedQuery(name="Tour.findAll", query="SELECT c FROM Tour c")
@Getter
@Setter
public class Tour implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name; 
	private String title; 
	@OneToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "game_id")
	private Game game; 
	private Long price;
	private Integer userCount;
	private boolean hot; 
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	private Long winPrice;
	private Long winItemId;
	private Long winUserId;
	private Integer position;
	private String photo;
	private String content;	
	private String rule;	
	private String type;	
	private String status;
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "tour", cascade = CascadeType.REMOVE)
	private Set<Result> result;
	
	public Tour() {
	}

	public Tour(TourRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public Tour(Integer tourId) {
		this.id = tourId;
	}

	public TourVO getVO() {
		TourVO response = new TourVO();
		BeanUtils.copyProperties(this, response);
		response.setGame(this.game.getVO());
		
		List<ResultVO> resultVOs = null;
		if(result !=null) {
			resultVOs = new ArrayList<>(result.size());
			for (Result result : result) {
				ResultVO resultVO = new ResultVO();
				BeanUtils.copyProperties(result, resultVO);
				UserDO userDO = new UserDO();
				BeanUtils.copyProperties(result.getUser(), userDO);
				resultVO.setUser(userDO);
				resultVOs.add(resultVO);
			}
		}
		response.setResult(resultVOs);
		
		return response;
	}
	
	public String toString() {
		return "id [" + this.id + "] [" +name + "]  game [" + this.game.getName() +"] ";
	}

}
