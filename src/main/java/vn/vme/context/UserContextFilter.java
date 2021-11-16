package vn.vme.context;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import vn.vme.common.JConstants;
import vn.vme.common.Utils;

@Component
public class UserContextFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(UserContextFilter.class);

	@Autowired
	Environment env;

	@Autowired(required = false)
	protected Flyway flyway;

	@Value("${spring.flyway.enabled}")
    boolean flywayEnable;
	
	 @Value("${server.port}")
	    String serverPort;
	 
	@Value("${jwt.signingKey}")
	String signingKey;
	
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;

		UserContextHolder.getContext().setCorrelationId(request.getHeader(UserContext.CORRELATION_ID));
		String accessToken = request.getHeader(JConstants.AUTH_HEADER);
		UserContextHolder.getContext().setAuthToken(accessToken, signingKey);
		filterChain.doFilter(request, servletResponse);
	}

	public void printOut(HttpServletRequest request) {
		Enumeration<String> map = request.getParameterNames();
		while (map.hasMoreElements()) {
			String paramName = map.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				log.debug("Filter request: " + paramName + ":" + paramValue);
			}
		}
	}

	 @Override
	    public void init(FilterConfig filterConfig) throws ServletException {
	        log.info("init User Context Filter server port " + serverPort + ", profile: " + env.getActiveProfiles()[0]);
	        if ((Utils.isLocal(env)) && flywayEnable) {
	            log.info("clean flywayEnable " + flywayEnable);
	            try {
	                flyway.clean();
	            } catch (Exception e) {
	                log.warn("clean " + e.getMessage());
	            }
	        }
	    }

	@Override
	public void destroy() {
	}
}