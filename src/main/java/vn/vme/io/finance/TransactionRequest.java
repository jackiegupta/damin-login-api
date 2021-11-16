package vn.vme.io.finance;



import lombok.Getter;
import lombok.Setter;
import vn.vme.common.DateUtils;
import vn.vme.common.Utils;
import vn.vme.entity.User;

/**
 * @m-tech
 */
@Getter
@Setter
public class TransactionRequest  {
	
	private Long id;
	private Long fromUserId;
	private Long toUserId;
	private String fromUserName;
	private User fromUser;	
	private User toUser;
	private Integer amount;
	private String type;
	private String typeChange;
	private String currency;
	private String title;
	private String content;
	private String status;	
	
	public String toString() {
		return this.fromUserId + "," + this.toUserId + "," + this.amount + "," + this.status;
	}

	public TransactionRequest(String name, Long fromUserId, Long toUserId, String status, String fromDate, String toDate) {
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
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.status = status;
		//this.fromDate = fromDate;
		//this.toDate = toDate;
	}
	
	public TransactionRequest() {
	}

	public TransactionRequest(Long id) {
		this.id = id;
	}
	
}
