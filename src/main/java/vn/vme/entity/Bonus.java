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
import vn.vme.io.game.BonusRequest;
import vn.vme.io.game.BonusVO;

@Entity
@Table(schema = "gate_schema", name = "bonus")
@NamedQuery(name = "Bonus.findAll", query = "SELECT c FROM Bonus c")
@Getter
@Setter
public class Bonus implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String title;
	private Integer levelId;
	private Integer rice;
	private Integer seed;
	private Integer expPoint;
	private String items;// format ID-Quantity: 1-3,2-1
	private String photo;	
	private String scope; //LEVELUP
	private String status;
	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	public Bonus() {
	}

	public Bonus(Integer bonusId) {
		this.id = bonusId;
	}

	public Bonus(BonusVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Bonus(BonusRequest request) {
		BeanUtils.copyProperties(request, this);
		
	}
	
	public BonusVO getVO() {
		BonusVO response = new BonusVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Bonus [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
