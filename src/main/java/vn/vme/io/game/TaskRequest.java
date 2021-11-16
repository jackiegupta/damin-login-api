package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest implements Serializable{

	protected Integer id;
	
	private String name;
	private String code;
	private Integer rice;
	private Integer seed;
	private Integer expPoint;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date endDate;
	
	private Long createId;
	private Long userId;
	private String status;	
	
	public TaskRequest() {

	}

	public TaskRequest(String key, Long userId, Integer eventId, String status) {
		this.name = key;
		this.userId = userId;
		this.name = key;
		this.status = status;
	}

	public String toString() {
		return id + "," + name;
	}
}
