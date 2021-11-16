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
import vn.vme.io.user.RoleVO;


/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name="role")
@NamedQuery(name="Role.findAll", query="SELECT g FROM Role g")
@Getter
@Setter
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String status;

	private String type;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	//bi-directional many-to-many association to User
	//@JsonIgnore
	//@ManyToMany(mappedBy="roles")
	//private List<User> users;

	public Role() {
	}

	public Role(int roleId) {
		this.id = roleId;
	}

	public RoleVO getVO() {
		RoleVO response = new RoleVO();
		BeanUtils.copyProperties(this, response);
		return response;
	}
	public String toString() {
		return id + "," + name ;
	}
}