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
import vn.vme.io.game.LevelsRequest;
import vn.vme.io.game.LevelsVO;

@Entity
@Table(schema = "gate_schema", name = "levels")
@NamedQuery(name = "Levels.findAll", query = "SELECT c FROM Levels c")
@Getter
@Setter
public class Levels implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String code;
	private String value;
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Levels() {
	}

	public Levels(LevelsVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Levels(LevelsRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Levels(Integer levelsId) {
		this.id = levelsId;
	}

	public LevelsVO getVO() {
		LevelsVO response = new LevelsVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Levels [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
