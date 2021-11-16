package vn.vme.io.game;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerRequest implements Serializable{

	protected Integer id;
	private String name;
	private String title;
	private String content;
	private String photo;
	private MultipartFile file;
	private Integer gameId;
	private String link;
	private Integer position;
	private String status;	
	
	public BannerRequest() {

	}

	public BannerRequest(String key, String status) {
		this.name = key;
		this.status = status;
	}

	public String toString() {
		return id + "," + name;
	}
}
