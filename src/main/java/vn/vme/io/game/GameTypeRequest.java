package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameTypeRequest implements Serializable {

	protected Integer id;

	private String name;

	private Integer position;
	
	private String content;

	private String photo;
	private MultipartFile file;

	private String status;

	private Long createId;

	protected Date createDate;

	public GameTypeRequest() {

	}

	public GameTypeRequest(String name, String content, String status) {
		this.name = name;
		this.content = content;
		this.status = status;
	}

	public String toString() {
		return id + "," + name;
	}
}
