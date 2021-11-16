package vn.vme.io.finance;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.DateUtils;
import vn.vme.common.Utils;

@Getter
@Setter
public class PaymentRequest {
	private Long id;
	protected String name;
	protected Long userId;
	private Integer productId;
	private String status;
	private String method;
	
	public PaymentRequest() {
	}

	public PaymentRequest(Long id) {
		this.id = id;
	}

	public PaymentRequest(String name, Long userId, Integer productId, String status, String fromDate, String toDate) {
		if (!fromDate.contains("undefined--") && Utils.isNotEmpty(fromDate) && DateUtils.toDateYYYYMMDD(fromDate) != null) {
			fromDate = fromDate + " 00:00:00";
		}else {
			fromDate = "";
		}
		if (!toDate.contains("undefined--") && Utils.isNotEmpty(toDate) && DateUtils.toDateYYYYMMDD(toDate) != null) {
			toDate = toDate + " 00:00:00";
		}else {
			toDate = "";
		}
		this.userId = userId;
		this.productId = productId;
		this.status = status;
		//this.fromDate = fromDate;
		//this.toDate = toDate;
	}
	public String toString() {
		return "id ["+ id + "] product [" + userId + "] status [" + status +"]";
	}
}
