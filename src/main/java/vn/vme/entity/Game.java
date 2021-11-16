package vn.vme.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.Utils;
import vn.vme.io.game.GameRequest;
import vn.vme.io.game.GameVO;

@Entity
@Table(schema = "gate_schema", name = "game")
@NamedQuery(name = "Game.findAll", query = "SELECT c FROM Game c")
@Getter
@Setter
public class Game implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	private String linkGame;
	
	private String content;
	
	// Loại game
	@ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.DETACH)
	@JoinColumn(name = "gameType", nullable = false)
	private GameType gameType;
	
	private Integer amount;
	private String currency;


	private String androidLink; // url android store

	private String iosLink; // url ios store

	private String windownLink; // url windown store

	private String homePage;

	private String fanPage;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "publish_id", nullable = false)
	private Publish publish; // nhà phát hành game
	
	private String platform;
	
	private Boolean news;
	private Boolean hot;
	private Boolean top;
	private Boolean h5sdk;
	private String h5desc;
	
	private Integer position;
	private String photo;
	
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	
	public Game() {
	}

	public Game(GameVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Game(GameRequest request) {
		Utils.copyNonNullProperties(request, this);
		//BeanUtils.copyProperties(request, this);
	}
	
	public Game(Integer gameId) {
		this.id = gameId;
	}

	public GameVO getVO() {
		GameVO response = new GameVO();
		BeanUtils.copyProperties(this, response);
		response.setGameType(gameType.getVO());
		response.setPublish(publish.getVO());
		return response;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(getVO());
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "Game [id=" + id + ", name=" + name + "]";
	}
	
}
