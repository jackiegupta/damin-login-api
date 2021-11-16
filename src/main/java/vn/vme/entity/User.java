package vn.vme.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.JConstants.Roles;
import vn.vme.common.JConstants.Status;
import vn.vme.common.JConstants.UserLevel;
import vn.vme.common.JConstants.UserType;
import vn.vme.common.Utils;
import vn.vme.io.user.RoleVO;
import vn.vme.io.user.SocialRequest;
import vn.vme.io.user.UserDO;
import vn.vme.io.user.UserFriendVO;
import vn.vme.io.user.UserRequest;
import vn.vme.io.user.UserVO;

/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(schema = "gate_schema", name = "user")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
@Getter
@Setter
public class User implements Serializable {

	@Id
	private Long id;
	private String userName;
	private String email;
	private String phone;
	private String fullName;
	private String password;
	private String gender;
	private String address;
	private String district;
	private String city;
	private String country;
	private String postCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	private String identity;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date identityDate;
	private String identityPlace;
	private String photo;
	private String verifyCode;
	private String verifyKey;
	private String type;
	private Boolean notify;
	private String provider;
	private String externalId;
	private Integer money;
	private Integer rice;
	private Integer seed;
	private Integer point;
	private Integer exp;
	private String level;
	private String vip;
	private String deviceId;
	private int loginCount;
	private String referCode;
	private String friendCode;
	private String status;
	private String attend;
	
	private Long userId360;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
	private Set<UserFriend> userFriends;

	// bi-directional many-to-many association to Role
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany()
	@JoinTable(schema = "gate_schema", name = "user_role", joinColumns = {
			@JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private List<Role> roles;

	public User() {
	}

	public User(Long id) {
		this.id = id;
	}

	public User(UserRequest request) {
		BeanUtils.copyProperties(request, this);
		List<RoleVO> roleVOs = request.getRoles();
		if (roles != null) {
			List<Role> roles = new ArrayList<>(roleVOs.size());
			for (RoleVO role : roleVOs) {
				roles.add(new Role(role.getId()));
			}
			this.setRoles(roles);
		}
	}

	public User(SocialRequest socialRequest) {
		BeanUtils.copyProperties(socialRequest, this);
	}

	@JsonIgnore
	public UserRequest getRequest() {
		UserRequest request = new UserRequest();
		BeanUtils.copyProperties(this, request);
		List<Role> roles = this.getRoles();
		List<RoleVO> roleVOs = null;
		if (roles != null && roles.size() > 0) {
			roleVOs = new ArrayList<RoleVO>();
			for (Role role : roles) {
				roleVOs.add(role.getVO());
			}
			request.setRoles(roleVOs);
		}
		return request;
	}

	public UserVO getVO() {
		UserVO response = new UserVO();
		List<RoleVO> roleVOs = null;
		List<UserFriendVO> userFriendVOs = null;
		if (roles != null) {
			roleVOs = new ArrayList<>(roles.size());
			for (Role role : roles) {
				roleVOs.add(role.getVO());
			}
		}
		if (userFriends != null) {
			userFriendVOs = new ArrayList<>(userFriends.size());
			for (UserFriend userFriend : userFriends) {
				UserFriendVO userFriendVO = new UserFriendVO();
				BeanUtils.copyProperties(userFriend, userFriendVO);
				User toFriend = userFriend.getFriend();
				UserDO to = new UserDO();// loop
				BeanUtils.copyProperties(toFriend, to);
				userFriendVO.setFriend(to);
				userFriendVO.setUserId(id);
				userFriendVOs.add(userFriendVO);
			}
		}
		BeanUtils.copyProperties(this, response);
		response.setRoles(roleVOs);
		response.setUserFriends(userFriendVOs);

		//response.setIdentity(Utils.mask(identity));
		// response.setPhone(Utils.mask(phone));
		return response;
	}

	@JsonIgnore
	public void addRole(Roles role) {
		if (roles == null) {
			roles = new ArrayList<Role>();
		}
		roles.add(new Role(role.ordinal()));
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
		return "User [id=" + id + ", userName=" + userName +", balance=" + money + "]";
	}

	public void setDefault() {
		this.addRole(Roles.USER);
		this.setStatus(Status.ACTIVE.name());
		this.setNotify(true);
		this.setType(UserType.PERSONAL.name());
		this.setLevel(UserLevel.BRONZE.name());
		this.setExp(0);
		this.setSeed(0);
		this.setMoney(0);
		this.setPoint(0);
		this.setCountry("VN");
		this.setCreateDate(new Date());
	}

}