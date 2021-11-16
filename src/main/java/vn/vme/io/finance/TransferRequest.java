package vn.vme.io.finance;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @m-tech
 */
@Getter
@Setter
public class TransferRequest  {
	
	private String receiver;
	private Long amount;
	private String type;
	private String typeChange;
	private String transferType;
	private String feeType;
	private Long fee;
	private String title;
	private String content;
	private String status;
	public String toString() {
		return this.receiver + "," + this.amount + "," + this.status;
	}

	public TransferRequest() {
	}


	public TransactionRequest getTransactionRequest() {
		TransactionRequest request = new TransactionRequest();
		BeanUtils.copyProperties(this,request);
		return request;
	}
	public TransferRequest(Long amount) {
		this.amount = amount;
	}
}
