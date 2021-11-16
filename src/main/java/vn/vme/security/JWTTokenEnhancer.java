package vn.vme.security;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import vn.vme.entity.User;
import vn.vme.io.user.UserVO;
import vn.vme.service.UserService;

public class JWTTokenEnhancer implements TokenEnhancer {
	private static final Logger log = LoggerFactory.getLogger(JWTTokenEnhancer.class);

    @Autowired
    private UserService userService;
    
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> additionalInfo = new HashMap<>();
		String authName = authentication.getName();
		User user = userService.findByUserNameOrEmailOrPhone(authName, authName,authName);
		log.debug("user " + user.getId());
		
		//UserVO userVO = userService.load(user.getVO().mask());
		UserVO userVO = userService.load(user.getVO());
		additionalInfo.put(User.class.getSimpleName().toLowerCase(), userVO);
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}
}
