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
import vn.vme.io.game.PublishRequest;
import vn.vme.io.game.PublishVO;

@Entity
@Table(schema = "gate_schema", name = "publish")
@NamedQuery(name = "Publish.findAll", query = "SELECT c FROM Publish c")
@Getter
@Setter
public class Publish implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	private Integer position;
	
	private String content;
	
	private String photo;
	
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Publish() {
	}

	public Publish(PublishVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Publish(PublishRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public PublishVO getVO() {
		PublishVO response = new PublishVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Publish [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
