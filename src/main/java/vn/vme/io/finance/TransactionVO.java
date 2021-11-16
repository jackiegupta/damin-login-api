package vn.vme.io.finance;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.JConstants.TransactionStatus;
import vn.vme.io.user.UserDO;
import vn.vme.io.user.UserVO;

/**
 * @m-tech
 */
@Getter
@Setter
public class TransactionVO implements Serializable{
	private Long id;
	@JsonIgnore
	private Long fromUserId;
	@JsonIgnore
	private Long toUserId;
	private Integer amount;
	private String type;
	private String typeChange;
	private String title;
	private String content;
	private String status;
	private Date createDate;
	private Date updateDate;
	protected UserDO fromUser;
	public String toString() {
		return this.id + "," +this.fromUserId +"=>" + this.toUserId + "," + this.amount + " VND," + this.status;
	}

	public TransactionVO() {
	}

	public TransactionVO(Long id) {
		this.id = id;
	}
	public TransactionVO(Long fromUserId, Long toUserId, Integer amount) {
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.amount = amount;
	}
	public boolean isCompleted() {
		return status.equals(TransactionStatus.COMPLETED.name()) 
				|| status.equals(TransactionStatus.APPROVED.name());
	}
	
}
