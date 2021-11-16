package vn.vme.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.game.CategoryNewsRequest;
import vn.vme.io.game.CategoryNewsVO;

@Entity
@Table(schema = "gate_schema", name = "category_news")
@NamedQuery(name = "CategoryNews.findAll", query = "SELECT c FROM CategoryNews c")
@Getter
@Setter
public class CategoryNews implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String title;
	
	private Integer position;
	private String photo;
	private String content;
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public CategoryNews() {
	}

	public CategoryNews(CategoryNewsVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public CategoryNews(CategoryNewsRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public CategoryNews(Integer eventId) {
		this.id = eventId;
	}

	public CategoryNewsVO getVO() {
		CategoryNewsVO response = new CategoryNewsVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}


	@Override
	public String toString() {
		return "CategoryNews [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
