package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.user.UserDO;

@Getter
@Setter
public class ResultVO implements Serializable{
	
	private Long id;
	
	private Integer tourId;
	private Long userId;
	private UserDO user;

	private int position;
	
	private String content;

	private boolean reply;
	
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	public ResultVO() {
	}
	public ResultVO(Long id) {
		this.id = id;
	}
	
	
	public String toString() {
		return "[id=" + this.id + "],[content=" + this.content + "],[userId=" + this.user + "],[reply=" + this.reply + "]";
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
