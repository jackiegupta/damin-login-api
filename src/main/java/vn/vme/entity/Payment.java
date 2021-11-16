package vn.vme.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.finance.PaymentRequest;
import vn.vme.io.finance.PaymentVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "payment")
@NamedQuery(name = "Payment.findAll", query = "SELECT c FROM Payment c")
@Getter
@Setter
public class Payment implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "product_id")
	private Product product;
	
	private String billId;
	private String refId;
	private int price;
	private String method;
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Payment() {
	}

	public Payment(PaymentRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public Payment(Long userId, Integer productId) {
		this.user = new User(userId);
		this.product = new Product(productId);
	}

	public PaymentVO getVO() {
		PaymentVO response = new PaymentVO();
		BeanUtils.copyProperties(this, response);
		response.setUser(user.getVO());
		response.setProduct(product.getVO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
