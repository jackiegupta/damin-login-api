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
import vn.vme.io.game.PartnerRequest;
import vn.vme.io.game.PartnerVO;

@Entity
@Table(schema = "gate_schema", name = "partner")
@NamedQuery(name = "Partner.findAll", query = "SELECT c FROM Partner c")
@Getter
@Setter
public class Partner implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String title;
	private Integer position;
	private String photo;
	private String content;
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Partner() {
	}

	public Partner(PartnerVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Partner(PartnerRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Partner(Integer supportId) {
		this.id = supportId;
	}

	public PartnerVO getVO() {
		PartnerVO response = new PartnerVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Partner [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
