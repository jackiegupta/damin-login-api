package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRequest implements Serializable {

	protected Integer id;

	private String name;

	private String linkGame;

	private String content;

	private Integer gameTypeId; // Loại game
	
	private Long price;
	
	private String currency;
	
	private Long amount;

	private String androidLink; // url android store

	private String iosLink; // url ios store

	private String windownLink; // url windown store

	private String homePage;

	private String fanPage;
	
	private Integer publishId; // nhà phát hành game
	
	private String platform;
	
	private Boolean news;
	private Boolean hot;
	private Boolean top;
	private Boolean h5sdk;
	private String h5desc;
	private Integer position;
	private String photo;
	private MultipartFile file;
	private String status;

	private Long createId;

	protected Date createDate;

	public GameRequest() {

	}

	public GameRequest(String name, Integer gameTypeId, String status) {
		this.name = name;
		this.gameTypeId = gameTypeId;
		this.status = status;
	}
	public GameRequest(String name, Integer gameTypeId, Integer publishId, String status, Boolean news, Boolean hot,Boolean top) {
		this.name = (name+"").toLowerCase();
		this.gameTypeId = gameTypeId;
		this.publishId = publishId;
		this.news = news;
		this.hot = hot;
		this.top = top;
		this.status = status;
	}

	public String toString() {
		return id + "," + name;
	}
}
