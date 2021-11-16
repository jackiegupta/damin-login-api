package vn.vme.io.finance;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @m-tech
 */
@Getter
@Setter
public class DepositVO implements Serializable{
	
	private Long id;
	private Long userId;
	private Long amount;
	private String bankCode;
	private String holderName;
	private String cardNumber;
	private String cardMonth;
	private String cardYear;
	private Long fee;
	private String serviceId;// ATM 
	private String payMethod; // CARD OR BANK_ACC
	private String status;
	public String toString() {
		return this.id + "," + this.userId + "," + this.amount + "," + this.payMethod;
	}

	public DepositVO() {
	}

	public DepositVO(Long id) {
		this.id = id;
	}
}
