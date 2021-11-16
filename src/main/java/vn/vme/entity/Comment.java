package vn.vme.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.notify.CommentRequest;
import vn.vme.io.notify.CommentVO;

/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "comment")
@NamedQuery(name = "Comment.findAll", query = "SELECT c FROM Comment c")
@Getter
@Setter
public class Comment implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	
	private String content;
	
	private String target;
	
	private boolean seen;

	private Long userId;
	
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	public Comment() {
	}

	public Comment(CommentRequest request) {
		BeanUtils.copyProperties(request, this);
	}

	
	
	public Comment(Long id) {
		this.id = id;
	}

	public CommentVO getVO() {
		CommentVO response = new CommentVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}

	public String toString() {
		return "[id=" + this.id + "],[content=" + this.content + "],[userId=" + this.userId + "]";
	}
}
