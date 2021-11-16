package vn.vme.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.game.GameTypeRequest;
import vn.vme.io.game.GameTypeVO;

@Entity
@Table(schema = "gate_schema", name = "game_type")
@NamedQuery(name = "GameType.findAll", query = "SELECT c FROM GameType c")
@Getter
@Setter
public class GameType implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	private Integer position;
	
	private String content;
	
	private String photo;
	
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public GameType() {
	}

	public GameType(GameTypeVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public GameType(GameTypeRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public GameTypeVO getVO() {
		GameTypeVO response = new GameTypeVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}

	@Override
	public String toString() {
		return "GameType [id=" + id + ", name=" + name + "]";
	}
	
}
