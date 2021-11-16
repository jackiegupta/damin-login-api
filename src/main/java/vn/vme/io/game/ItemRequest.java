package vn.vme.io.game;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest implements Serializable{

	protected Long id;
	private String name;
	private String title;
	private Long price;
	private String currency;
	private String photo;
	private MultipartFile file;
	private Integer gameId;
	private Long userId;
	private String status;	
	
	public ItemRequest() {

	}

	public ItemRequest(String key, Long userId, String status) {
		this.name = key;
		this.userId = userId;
		this.status = status;
	}

	public String toString() {
		return id + "," + name;
	}
}
