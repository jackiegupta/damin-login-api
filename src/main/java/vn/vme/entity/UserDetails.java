package vn.vme.entity;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetails extends org.springframework.security.core.userdetails.User {
	private String userId;
	private String email;
	private String fullName ;
	private boolean using2FA;
	private String provider;

	public UserDetails(String userId, String email, String fullName ,  String password, boolean isUsing2FA,
			List<GrantedAuthority> authorities) {
		super(email, password, authorities);
		this.userId = userId;
		this.email=email;
		this.using2FA = isUsing2FA; 
		this.fullName =fullName ;
	}
	
	public UserDetails(String userId, String fullName ,
			List<GrantedAuthority> authorities, String provider) {
		super(userId, "", authorities);
		this.userId = userId; 
		this.fullName =fullName ;
		this.provider=provider;
	} 
 
	
	public UserDetails(String userId, String email, String fullName ,  String lastName ,
			List<GrantedAuthority> authorities, String provider) {
		super(email, "", authorities);
		this.userId = userId;  
		this.email=email;
		this.fullName =fullName ;
		this.provider=provider;
	}
	
	
}
