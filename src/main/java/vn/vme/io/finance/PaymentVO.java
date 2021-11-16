package vn.vme.io.finance;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.game.ProductVO;
import vn.vme.io.user.UserVO;

@Getter
@Setter
public class PaymentVO {
	private Long id;
	protected UserVO user;
	private ProductVO product;
	private String status;
	private String billId;
	private String refId;
	private int price;
	private String method;
	
	public PaymentVO() {
	}

	public PaymentVO(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] product [" + product.getId() + "] status [" + status +"]";
	}
}
