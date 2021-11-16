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
import vn.vme.io.game.VippointRequest;
import vn.vme.io.game.VippointVO;

@Entity
@Table(schema = "gate_schema", name = "vippoint")
@NamedQuery(name = "Vippoint.findAll", query = "SELECT c FROM Vippoint c")
@Getter
@Setter
public class Vippoint implements Serializable {
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
	

	public Vippoint() {
	}

	public Vippoint(VippointVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Vippoint(VippointRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Vippoint(Integer vippointId) {
		this.id = vippointId;
	}

	public VippointVO getVO() {
		VippointVO response = new VippointVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Vippoint [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
