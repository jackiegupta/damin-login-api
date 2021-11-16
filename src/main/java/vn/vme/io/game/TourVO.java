package vn.vme.io.game;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import vn.vme.common.DateUtils;
import vn.vme.common.JConstants;
import vn.vme.common.JConstants.TourStatus;
import vn.vme.io.user.UserDO;
import vn.vme.io.user.UserFriendVO;

@Getter
@Setter
public class TourVO implements Serializable {

	@ApiModelProperty(position = 1)
	protected Integer id;
	private String name; 
	private String title; 
	private GameVO game; 
	private Long price;
	private Integer userCount;
	private boolean hot; 
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	private Long winPrice;
	private Long winProductId;
	private Long winUserId;
	private UserDO winUser;
	private Integer position;
	private String photo;
	private String content;	
	private String rule;	
	private String type;	
	private String status;
	private Long createId;
	
	private List<ResultVO> result;

	public TourVO() {
	}

	public TourVO(Integer id) {
		this.id = id;
	}

	public String getStartClock() {
		if (startDate == null) {
			return "";
		}
		return DateUtils.toString(startDate, JConstants.HHmm);

	}

	/*
	 * public String getStartDate() { if (startDate == null) { return ""; } return
	 * DateUtils.toString(startDate, JConstants.DD_MM_YYYY); }
	 */


	public boolean isFinish() {
		return TourStatus.FINISHED.name().equals(status);
	}


	public String toString() {
		return "id [" + this.id + "] [" + this.startDate + "] [" + this.price + "] ";
	}
	
}
