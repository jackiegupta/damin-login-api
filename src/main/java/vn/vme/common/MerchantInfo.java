package vn.vme.common;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * @m-tech Product session accessToken info for current school of student, employee
 */
@Getter
@Setter
public class MerchantInfo {
	private long id;
	private String phone;
	private String userName;
	private String email;
	private String name;
	private BigDecimal feeRate;
	private String paypalUser;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}

	public String getPaypalUser() {
		return paypalUser;
	}

	public void setPaypalUser(String paypalUser) {
		this.paypalUser = paypalUser;
	}

}
