package vn.vme.io.game;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest implements Serializable{

	private Integer id;
	private Integer price;// VND
	private Integer amount;
	private String currency;
	private Integer discount;
	private Integer itemId;
	private Integer exp;
	private String status;	
	private Integer createId;
	
	public ProductRequest() {
	}

	public ProductRequest(Long userId, Integer price, Integer amount, String currency,String status) {
		this.price = price;
		this.amount = amount;
		this.currency = currency;
		this.status = status;
	}

	public String toString() {
		return id + "," + amount;
	}
}
