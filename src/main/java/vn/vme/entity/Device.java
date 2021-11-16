package vn.vme.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.user.DeviceVO;

/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "device")
@NamedQuery(name = "Device.findAll", query = "SELECT u FROM Device u")
@Getter
@Setter
public class Device implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String deviceId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date loginTime;
	
	private long userId;	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;


	public Device() {
	}

	public Device(Long id) {
		this.id = id;
	}

	public Device(DeviceVO request) {
		BeanUtils.copyProperties(request, this);
	}

	public Device(String deviceId) {
		this.deviceId = deviceId;
	}

	@JsonIgnore
	public DeviceVO getRequest() {
		DeviceVO request = new DeviceVO();
		BeanUtils.copyProperties(this, request);
		
		return request;
	}

	public DeviceVO getVO() {
		DeviceVO response = new DeviceVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", deviceId=" + deviceId + "]";
	}
}