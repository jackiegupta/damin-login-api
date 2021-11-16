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
import vn.vme.io.notify.NotificationVO;

@Entity
@Table(schema = "gate_schema", name = "notify")
@NamedQuery(name = "Notify.findAll", query = "SELECT c FROM Notify c")
@Getter
@Setter
public class Notify implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long userId;
	
	private String title;
	
	private String shortContent;
	
	private String fullContent;
	
	private String photo;
	
	private String status;
	
	private String type;
	
	private String target;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Notify() {
	}

	
	public NotificationVO getVO() {
		NotificationVO response = new NotificationVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Notify [id=" + id + ", title=" + title + "]";
	}


	
	
	
}
