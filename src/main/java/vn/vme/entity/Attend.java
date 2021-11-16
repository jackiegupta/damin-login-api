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
import vn.vme.io.game.AttendRequest;
import vn.vme.io.game.AttendVO;

@Entity
@Table(schema = "gate_schema", name = "attend")
@NamedQuery(name = "Attend.findAll", query = "SELECT c FROM Attend c")
@Getter
@Setter
public class Attend implements Serializable {
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
	

	public Attend() {
	}

	public Attend(AttendVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Attend(AttendRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Attend(Integer attendId) {
		this.id = attendId;
	}

	public AttendVO getVO() {
		AttendVO response = new AttendVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Attend [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
