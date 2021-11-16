package vn.vme.io.finance;



import lombok.Getter;
import lombok.Setter;

/**
 * @m-tech
 */
@Getter
@Setter
public class VitapayTransactionRequest  {
	
	private Long id;
	private String customerId;
	private Long amount;
	private String providerCode;
	private String serviceCode;
	private String billId;
	private String channel;
	private String refId;
	private String payerId;
	private String payerPhone;
	private String returnUrl;
	private String backUrl;
	private String bankCode;
	private String paymentMethod;
	private int productId;
	private String methodSelected;
	
	
	public String toString() {
		return this.customerId + "," + this.providerCode + "," + this.amount + "," + this.billId;
	}

	
	public VitapayTransactionRequest() {
	}

	public VitapayTransactionRequest(Long id) {
		this.id = id;
	}
	
}
