package vn.vme.io.game;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.user.UserVO;

@Getter
@Setter
public class UserGiftcodeVO {
	private Long id;
	protected UserVO user;
	private GiftcodeVO giftcode;
	private String status;
	private Date createDate;
	public UserGiftcodeVO() {
	}

	public UserGiftcodeVO(Long id) {
		this.id = id;
	}

	public String toString() {
		return "id ["+ id + "] giftcode [" + giftcode.getId() + "] status [" + status +"]";
	}
}
