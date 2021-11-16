package vn.vme.io.finance;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.user.UserVO;

/**
 * @m-tech
 */
@Getter
@Setter
public class TransferVO implements Serializable{
	private String id;
	private UserVO toUser;
	private Long amount;
	private Long fee;
	private Long captureAmount;
	private String type;
	private String typeChange;
	private String transferType;
	private String feeType;
	private String title;
	private String content;
	private String status;
	private Date createDate;
	private Date updateDate;
	public String toString() {
		return this.toUser.getId() + "," + this.amount + "," + this.content + "," + this.status;
	}

	public TransferVO() {
	}

	public TransferVO(TransactionRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	public TransferVO(TransferRequest request) {
		BeanUtils.copyProperties(request, this);
	}

}
