package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVO implements Serializable{
	
	private Integer id;
	private Integer price;// VND
	private Integer amount;
	private String currency;
	private Integer discount;
	private Integer exp;
	private String status;	
	private Integer createId;
	private Date createDate;
	private Date updateDate;
	
	public ProductVO() {
	}
	public ProductVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[price=" + this.price + "]";
	}
}
