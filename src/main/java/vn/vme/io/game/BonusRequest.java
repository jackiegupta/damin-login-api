package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BonusRequest implements Serializable{

	private Integer id;
	
	private String name;
	
	private String title;
	
	private Integer levelId;
	
	private String scope;
	
	private Integer rice;
	
	private Integer seed;
	
	private Integer expPoint;
	
	private String items;// format ID-Quantity: 1-3,2-1
	
	private String status;
	
	private String photo;
	
	private MultipartFile file;
	
	private Long createId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date startDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date endDate;
	
	public BonusRequest() {

	}

	public BonusRequest(String name, Integer levelId, String scope, String status) {
		this.name = name;
		this.levelId = levelId;
		this.scope = scope;
		this.status = status;
	}
	
	
	public String toString() {
		return id + "," + name;
	}
}
