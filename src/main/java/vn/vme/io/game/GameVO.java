package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameVO implements Serializable{
	
	protected Integer id;
	
	private String name;
	
	private String linkGame;
	
	private String content;
	
	private GameTypeVO gameType; // Loại game
	
	private Integer amount;
	
	private String currency;

	private String androidLink; // url android store

	private String iosLink; // url ios store

	private String windownLink; // url windown store

	private String homePage;

	private String fanPage;
	
	private PublishVO publish; // nhà phát hành game
	
	private String platform;
	
	private Boolean news;
	private Boolean hot;
	private Boolean top;
	private Boolean h5sdk;
	private String h5desc;
	
	private Integer position;
	private String photo;
	
	private String status;
	
	private Long createId;
	
	protected Date createDate;
	
	public GameVO() {
	}
	public GameVO(Integer id) {
		this.id = id;
	}
	public String toString() {
		return "[id=" + this.id + "],[name=" + this.name + "]";
	}
}
