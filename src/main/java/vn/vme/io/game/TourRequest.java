package vn.vme.io.game;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TourRequest {

	@ApiModelProperty(position = 1)
	private Integer id;
	private String name; 
	private String title; 
	//private GameVO game; 
	private Integer gameId; 
	private Long price;
	private Integer userCount;
	private boolean hot; 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date endDate;
	private Long winPrice;
	private Long winItemId;
	private Long winUserId;
	private Integer position;
	private String photo;
	private MultipartFile file;
	private String content;	
	private String rule;	
	private String status;
	private Long createId;
	
	@JsonIgnore
	private Long fromPrice, toPrice;
	@JsonIgnore
	private String fromDate, toDate;
	
	
	public String toString() {
		return "id [" + this.id + "]";
	}

	public TourRequest() {
	}

	public TourRequest(String key, Long fromPrice, Long toPrice, String fromDate, String toDate, String status) {
		this.name = key;
		this.fromPrice = fromPrice;
		this.toPrice = toPrice;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.status = status;
	}

}
