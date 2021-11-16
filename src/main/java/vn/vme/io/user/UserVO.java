package vn.vme.io.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.DateUtils;
import vn.vme.common.JConstants.Roles;
import vn.vme.common.JConstants.UserLevel;
import vn.vme.common.JConstants.Vip;
import vn.vme.common.Utils;

@Getter
@Setter
public class UserVO extends UserDO{
	
	private String email;
	private String phone;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date birthDate;
	private String phoneStatus;
	@JsonIgnore
	private String verifyKey;
	@JsonIgnore
	private String verifyCode;
	private String identity;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date identityDate;
	private String identityPlace;
	
	private String gender;
	private String address;
	private String district;
	private String deviceId;
	private String country;
	private String postCode;
	private Boolean notify;
	private String provider;
	private String externalId;
	
	private Integer money;
	private Integer rice;
	private Integer seed;
	private Integer point;
	private Integer exp;
	
	private Integer pointCount;
	private Integer expCount;
	private String nextLevel;
	private String nextVip;
	
	private String level;
	private String vip;
	boolean topLevel;
	boolean topVip;
	
	private String referCode;
	private String referLink;
	private String friendCode;
	private String status;
	
	private String attend;
	
	private Date createDate;
	private Date updateDate;
	private List<RoleVO> roles;
	private List<UserFriendVO> userFriends;
	public UserVO() {
	}
	public UserVO(Long id) {
		this.id = id;
	}
	public UserVO(UserDO userDO) {
		BeanUtils.copyProperties(userDO, this);
	}
	public String getBirthDate() {
		if (birthDate == null) {
			return "";
		}
		return DateUtils.toString(birthDate);
	}
	public boolean isTopLevel() {
		return Utils.isNotEmpty(level) && level.equals(UserLevel.PLATIN.name());
	}
	public boolean isTopVip() {
		return Utils.isNotEmpty(vip) && vip.equals(Vip.VIP3.name());
	}
	@JsonIgnore
	public UserVO mask() {
		this.phone = Utils.mask(this.phone);
		this.email = Utils.maskEmail(this.email);
		return this;
	}
	
	@JsonIgnore
	public boolean isAdmin() {
		if (roles != null) {
			for (RoleVO roleVO : roles) {
				String role = roleVO.getType();
				if (role.equals(Roles.ADMIN.name()) 
						|| role.equals(Roles.GM.name())
						|| role.equals(Roles.SUPPORT.name())) {
					return true;
				}
			}
		}
		return false;
	}
	
	@JsonIgnore
	public boolean isRoot() {
		if(roles!=null) {
			for (RoleVO roleVO : roles) {
				String role = roleVO.getType();
				if(role.equals(Roles.ROOT.name())) {
					return true;
				}	
			}
		}
		return false;
	}
	@JsonIgnore
	public String getHash() {
		return email + id;
	}
	
	public String toString() {
		return id + "," + userName;
	}
	public Long getCreateDate() {
		if (createDate != null) {
			return createDate.getTime();
		}else {
			return null;
		}
	}
	
	public Long getUpdateDate() {
		if (updateDate != null) {
			return updateDate.getTime();
		}else {
			return null;
		}
	}
	@JsonIgnore
	public UserDO getDO() {
		UserDO userDO = new UserDO();
		BeanUtils.copyProperties(this, userDO);
		return userDO;
	}
}
