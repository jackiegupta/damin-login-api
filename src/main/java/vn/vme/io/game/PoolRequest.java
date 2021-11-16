package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoolRequest implements Serializable{

	private Integer id;
	
	private String name;
	private String title;
	private String content;
	private String code;
	
	private Integer count;
	
	private Integer size;
	
	private String type; //ONE, MULTI, ALL
	
	private String scope;	//EVENT, GAME
	
	private Integer rice;
	private Integer seed;
	private Integer expPoint;
	private String items;// format ID-Quantity: 1-3,2-1
	
	private Integer eventId;
	
	private Integer gameId;
	
	private String gameName;	
	
	private String status;	
	
	private String photo;
	private MultipartFile file;
	
	private Long createId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date endDate;
	
	public PoolRequest() {

	}

	public PoolRequest(String type, String scope, String status) {
		this.type = type;
		this.scope = scope;
		this.status = status;
		this.name = status;
	}
	public PoolRequest(String type, String scope, String status, String gameName) {
		this.type = type;
		this.scope = scope;
		this.status = status;
		this.gameName = gameName;
	}
	
	public String toString() {
		return id + "," + count;
	}
}
