package vn.vme.model;

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
import vn.vme.io.notify.NotificationVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "notify_schema", name = "notification")
@NamedQuery(name = "Notification.findAll", query = "SELECT c FROM Notification c")
@Getter
@Setter
public class Notification implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long userId;
	
	private String title;
	
	private String shortContent;
	
	private String fullContent;
	
	private String photo;
	
	private String status;
	
	private String type;
	
	private String target;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Notification() {
	}

	
	public NotificationVO getVO() {
		NotificationVO response = new NotificationVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Notification [id=" + id + ", userId=" + userId + ", title=" + title + ", shortContent=" + shortContent
				+ ", fullContent=" + fullContent + ", status=" + status + ", type=" + type + ", target=" + target
				+ ", createDate=" + createDate + ", updateDate=" + updateDate + "]";
	}


	
	
	
}
