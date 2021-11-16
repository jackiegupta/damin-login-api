package vn.vme.io.game;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.DateUtils;
import vn.vme.common.Utils;

@Getter
@Setter
public class NewsRequest implements Serializable{

	private Integer id;
		
	private String name;
	private String title;
	private Integer categoryId;
	private Integer position;
	private String photo;
	private MultipartFile file;
	private String content;
	private String status;	
	@JsonIgnore
	private String fromDate, toDate;
	
	public NewsRequest() {

	}

	public NewsRequest(String name, Integer categoryId, String status, String fromDate, String toDate) {
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
		this.categoryId = categoryId;
		this.status = status;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public String toString() {
		return id + "," + name;
	}
}
