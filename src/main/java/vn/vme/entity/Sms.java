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

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.notify.SmsRequest;
import vn.vme.io.notify.SmsVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "sms")
@NamedQuery(name = "Sms.findAll", query = "SELECT c FROM Sms c")
@Getter
@Setter
public class Sms implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String phone;

	private String sender;

	private String subject;
	private String message;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	private String status;

	public Sms() {
	}

	public Sms(String phone, String message) {
		this.phone = phone;
		this.message = message;
	}
	
	public Sms(SmsRequest request) {
		BeanUtils.copyProperties(request, this);
	}


	public SmsVO getVO() {
		SmsVO response = new SmsVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}

	public String toString() {
		return this.id + "," + this.subject + "," + this.phone;
	}
}
