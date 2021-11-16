package vn.vme.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.game.BannerRequest;
import vn.vme.io.game.BannerVO;

@Entity
@Table(schema = "gate_schema", name = "banner")
@NamedQuery(name = "Banner.findAll", query = "SELECT c FROM Banner c")
@Getter
@Setter
public class Banner implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String title;
	private String content;
	private String photo;
	private String link;
	
	@OneToOne(cascade= CascadeType.DETACH)
	@JoinColumn(name = "game_id")
	private Game game;
	
	private Integer position;
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Banner() {
	}

	public Banner(BannerVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Banner(BannerRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Banner(Integer bannerId) {
		this.id = bannerId;
	}

	public BannerVO getVO() {
		BannerVO response = new BannerVO();
		BeanUtils.copyProperties(this, response);
		if (game != null) {
			response.setGame(game.getVO());
		}
		return response;
	}


	@Override
	public String toString() {
		return "Banner [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
