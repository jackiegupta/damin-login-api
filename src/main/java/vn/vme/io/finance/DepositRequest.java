package vn.vme.io.finance;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.VO;

/**
 * @m-tech
 */
@Getter
@Setter
public class DepositRequest  {
	private Long id;
	private String tid;
	private Long amount;
	private String type;
	private Integer bankId;
	private String content;
	private String note;
	private VO status;
	public String toString() {
		return this.amount + "," + this.content + "," + this.status;
	}

	public DepositRequest() {
	}

	public DepositRequest(String tid) {
		this.tid = tid;
	}
}
