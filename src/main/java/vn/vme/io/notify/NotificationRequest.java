package vn.vme.io.notify;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Data
public class NotificationRequest {
	
	protected Long id;

	protected Long userId;
	
	protected List<Long> userIds;
	
	protected String title;

	protected String shortContent;

	protected String fullContent;
	
	private String status;
	
    private String type;
	
	private String target;

	protected Date createDate;
	
	protected Date updateDate;
	
	private String photo;

	public NotificationRequest() {
	}	

}
