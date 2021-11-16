package vn.vme.io.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceVO implements Serializable {
	protected Long id;

	private String deviceId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date loginTime;
	
	private long loginCount;
	
	private long userCount;

	private long userId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	public DeviceVO() {
	}

	public DeviceVO(int loginCount) {
		this.loginCount = loginCount;
	}

	public String toString() {
		return deviceId;
	}
}
