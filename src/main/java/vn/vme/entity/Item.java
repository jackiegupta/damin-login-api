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
import vn.vme.io.game.ItemRequest;
import vn.vme.io.game.ItemVO;

@Entity
@Table(schema = "gate_schema", name = "item")
@NamedQuery(name = "Item.findAll", query = "SELECT c FROM Item c")
@Getter
@Setter
public class Item implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String title;
	private Long price;
	private String currency;
	private String photo;
	private Integer gameId;
	private Integer position;
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Item() {
	}

	public Item(ItemVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Item(ItemRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Item(Long itemId) {
		this.id = itemId;
	}

	public ItemVO getVO() {
		ItemVO response = new ItemVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
