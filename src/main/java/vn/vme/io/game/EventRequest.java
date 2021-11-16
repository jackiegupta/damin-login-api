package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.DateUtils;
import vn.vme.common.Utils;

@Getter
@Setter
public class EventRequest implements Serializable{

	private Integer id;
		
	private String name;
	private String title;
	
	private Integer giftCount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
	private Date endDate;
	private Integer position;
	private String photo;
	private MultipartFile file;
	private String content;
	private String status;	
	
	private String pools;	
	private String games;	
	
	@JsonIgnore
	private String fromDate, toDate;
	
	public EventRequest() {

	}

	public EventRequest(String name, String status, String fromDate, String toDate) {
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
		this.name = name;
		this.status = status;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public String toString() {
		return id + "," + name;
	}
}
