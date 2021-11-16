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
import vn.vme.io.game.SupportRequest;
import vn.vme.io.game.SupportVO;

@Entity
@Table(schema = "gate_schema", name = "support")
@NamedQuery(name = "Support.findAll", query = "SELECT c FROM Support c")
@Getter
@Setter
public class Support implements Serializable {
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
	

	public Support() {
	}

	public Support(SupportVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Support(SupportRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Support(Integer supportId) {
		this.id = supportId;
	}

	public SupportVO getVO() {
		SupportVO response = new SupportVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Support [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
