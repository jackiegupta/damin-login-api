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
import vn.vme.io.finance.TransactionRequest;
import vn.vme.io.finance.TransactionVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "transaction")
@NamedQuery(name = "Transaction.findAll", query = "SELECT c FROM Transaction c")
@Getter
@Setter
public class Transaction implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "from_user_id")
	private User fromUser;
	
	@ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name = "to_user_id")
	private User toUser;
	private Integer amount;
	private String type;
	private String typeChange;
	private String currency;
	private String title;
	private String content;
	private String status;	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	public Transaction() {
	}

	public Transaction(TransactionRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	public Transaction(Long fromUserId, Long toUserId) {
	}

	public Transaction(Integer amount, String title) {
		this.amount = amount;
		this.title = title;
	}

	public TransactionVO getVO() {
		TransactionVO response = new TransactionVO();
		BeanUtils.copyProperties(this, response);
		response.setFromUser(fromUser.getVO().getDO());
		return response;
	}

	public String toString() {
		return this.id + "," + this.status;
	}
}
