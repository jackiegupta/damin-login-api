package vn.vme.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import vn.vme.common.JConstants.Status;
import vn.vme.entity.User;
import vn.vme.exception.UserNotConfirmedException;
import vn.vme.repository.UserRepository;

/**
 * @m-tech
 */
@Component
public class JWTUserDetailsService implements UserDetailsService {
	private static final Logger log = LoggerFactory.getLogger(JWTUserDetailsService.class);
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) {
		
		log.debug("loadUserByUsername {}", userId);
		
		User user = userRepository.findByUserNameOrEmailOrPhone(userId, userId,userId);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("The userId %s doesn't exist", userId));
		}
		if (user.getStatus().equals(Status.INACTIVE.name())) {
			throw new UserNotConfirmedException("The email "+ user.getEmail()+" net yet verify");
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getType()));
		});

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);

		return userDetails;
	}
}
