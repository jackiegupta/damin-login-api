package vn.vme.io.game;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishRequest implements Serializable{

	private Integer id;
	
	private String name;
	
	private Integer position;
	
	private String content;
	
	private String photo;
	private MultipartFile file;
	private String status;	
	
	
	public PublishRequest() {

	}

	public PublishRequest(String key,  String status) {
		this.name = key;
		this.status = status;
	}

	public String toString() {
		return id + "," + name;
	}
}
