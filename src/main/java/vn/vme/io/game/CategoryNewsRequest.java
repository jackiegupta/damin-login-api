package vn.vme.io.game;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryNewsRequest implements Serializable{

	private Integer id;
	private String name;
	private String title;
	private Integer position;
	private String photo;
	private MultipartFile file;
	private String content;
	private String status;
	public CategoryNewsRequest() {

	}

	public CategoryNewsRequest(String name, String status) {
		this.name = name;
		this.status = status;
	}

	public String toString() {
		return id + "," + name;
	}
}
