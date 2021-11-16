package vn.vme.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import vn.vme.common.JConstants;
import vn.vme.io.user.RoleVO;
import vn.vme.io.user.UserVO;

@Component
public class UserContext {
	 private static final Logger log = LoggerFactory.getLogger(UserContext.class);

	public static final String CORRELATION_ID = "mtech-correlation-id";
	public static final String AUTH_TOKEN = "mtech-auth-token";
	public static final String MERCHANT_ID = "mtech-merchant-id";
	public static final String WALLET = "mtech-wallet";
	public static final String USER = "mtech-user";

	private static final ThreadLocal<String> correlationId = new ThreadLocal<String>();
	private static final ThreadLocal<String> authToken = new ThreadLocal<String>();
	private static final ThreadLocal<UserVO> currentUser = new ThreadLocal<UserVO>();

	public String getCorrelationId() {
		return correlationId.get();
	}

	public void setCorrelationId(String cid) {
		correlationId.set(cid);
	}

	public UserVO getCurrentUser() {
		return currentUser.get();
	}

	public void setCurrentUser(UserVO aUser) {
		currentUser.set(aUser);
	}

	public String getAuthToken() {
		return authToken.get();
	}

	public void setAuthToken(String accessToken,String signingKey) {
		if (StringUtils.isEmpty(accessToken) || !accessToken.startsWith(JConstants.BEARER)) {
			return;
		}	
		accessToken = accessToken.replace(JConstants.BEARER, "").trim();
		try {
			Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(signingKey)).isSigned(accessToken);
			Jwt jwt = JwtHelper.decode(accessToken);
			Map<String, Object> claims = new ObjectMapper().readValue(jwt.getClaims(), Map.class);
		
			Map user = (Map) claims.get("user");
			UserVO userVO =  new ObjectMapper().convertValue(user, UserVO.class);
			if(userVO.getRoles()==null || userVO.getRoles().size()==0){
				List<RoleVO> roles = new ArrayList<RoleVO>();
				roles.add(new RoleVO(5));
				userVO.setRoles(roles);
			}
			this.setCurrentUser(userVO);
			log.info("Current userVO:" + userVO.getId());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		authToken.set(accessToken);
	}
}