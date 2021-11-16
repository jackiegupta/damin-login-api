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
import vn.vme.io.game.NewsRequest;
import vn.vme.io.game.NewsVO;

@Entity
@Table(schema = "gate_schema", name = "news")
@NamedQuery(name = "News.findAll", query = "SELECT c FROM News c")
@Getter
@Setter
public class News implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String title;
	
	@OneToOne(cascade= CascadeType.DETACH)
	@JoinColumn(name = "category_id")
	private CategoryNews categoryNews;
	
	private Integer position;
	private String photo;
	private String content;
	private String status;	
	private Long createId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	

	public News() {
	}

	public News(NewsVO request) {
		BeanUtils.copyProperties(request, this);
	}
	public News(NewsRequest request) {
		BeanUtils.copyProperties(request, this);
	}
	
	public News(Integer newsId) {
		this.id = newsId;
	}

	public NewsVO getVO() {
		NewsVO response = new NewsVO();
		BeanUtils.copyProperties(this, response);
		if (categoryNews != null) {
			response.setCategoryNews(categoryNews.getVO());
		}
		return response;
	}


	@Override
	public String toString() {
		return "News [id=" + id + ", name=" + name + "]";
	}


	
	
	
}
