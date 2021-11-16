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
import vn.vme.io.game.SettingRequest;
import vn.vme.io.game.SettingVO;

@Entity
@Table(schema = "gate_schema", name = "setting")
@NamedQuery(name = "Setting.findAll", query = "SELECT c FROM Setting c")
@Getter
@Setter
public class Setting implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String code;
	private String value;
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public Setting() {
	}

	public Setting(SettingVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public Setting(SettingRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public Setting(Integer settingId) {
		this.id = settingId;
	}

	public SettingVO getVO() {
		SettingVO response = new SettingVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "Setting [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
