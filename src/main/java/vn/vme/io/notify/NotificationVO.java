package vn.vme.io.notify;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationVO implements Serializable {

	protected Long id;

	protected Long userId;
	
	protected String title;

	protected String shortContent;

	protected String fullContent;
	
	private String status;
    
	private String type;
	
	private String target;

	protected Date createDate;
	
	protected Date updateDate;
	
	private String photo;

	public NotificationVO() {
	}

}
