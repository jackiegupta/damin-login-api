package vn.vme.io.notify;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVO implements Serializable{
	
	private Long id;
	
	private String title;
	
	private String content;
	
	private String target;
	
	private boolean seen;

	private Long userId;
	
	private String status;
	
	private Date createDate;
	
	private Date updateDate;
	
	public CommentVO() {
	}
	public CommentVO(Long id) {
		this.id = id;
	}
	
	
	public String toString() {
		return "[id=" + this.id + "],[content=" + this.content + "],[userId=" + this.userId + "]";
	}
	public Long getCreateDate() {
		return createDate.getTime();
	}
	
	public Long getUpdateDate() {
		if (updateDate != null) {
			return updateDate.getTime();
		}else {
			return null;
		}
	}
}
