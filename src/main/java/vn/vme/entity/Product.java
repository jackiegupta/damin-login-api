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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.game.ProductRequest;
import vn.vme.io.game.ProductVO;

@Entity
@Table(schema = "gate_schema", name = "product")
@NamedQuery(name = "Product.findAll", query = "SELECT c FROM Product c")
@Getter
@Setter
public class Product implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer price;// VND
	private Integer amount;
	private String currency;
	private Integer discount;
	private Integer exp;
	private String status;	
	private Integer createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Product() {
	}

	public Product(ProductVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Product(ProductRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Product(Integer id) {
		this.id = id;
	}

	public ProductVO getVO() {
		ProductVO response = new ProductVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(getVO());
			System.out.println("ResultingJSONstring = " + json);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "Product [id=" + id + ", price=" + price + ",amount=" + amount + ",currency=" + currency + ",discount=" + discount+ "]";
		
	}
}
