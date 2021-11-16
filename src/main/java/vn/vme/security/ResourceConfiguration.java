package vn.vme.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import vn.vme.common.URI;

@Configuration
public class ResourceConfiguration extends ResourceServerConfigurerAdapter{
    @Override
    public void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
    	 http.authorizeRequests()
		//.antMatchers(HttpMethod.GET).hasIpAddress("127.0.0.1")// serviceUrl api
    	.antMatchers(HttpMethod.GET).permitAll()//hasIpAddress("127.0.0.1")// serviceUrl api
	    .antMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
		.antMatchers(HttpMethod.POST, 
				URI.V1 + URI.DEVICE,
				URI.V1 + URI.USER + URI.CHECK,
				URI.V1 + URI.USER + URI.VERIFY,
				URI.V1 + URI.OAUTH_TOKEN,
				URI.V1 + URI.USER + URI.REGISTER, 
				URI.V1 +  URI.USER + URI.PHOTO,
				URI.V1 +  URI.USER + URI.PASSWORD,
				URI.V1 +  URI.USER + URI.PASSWORD + URI.VERIFY,
				URI.V1 +  URI.SOCIAL + URI.SOCIAL,
				URI.V1 + URI.SOCIAL).permitAll()
		 //.antMatchers(HttpMethod.GET, URI.V1 +  URI.USER +"/*").hasAnyAuthority("USER")
         .antMatchers(HttpMethod.POST).hasAnyAuthority("ROOT","USER","GM","SUPPORT","ADMIN")
         .antMatchers(HttpMethod.PUT).hasAnyAuthority("ROOT","USER","GM","SUPPORT","ADMIN")
 		 .antMatchers(HttpMethod.DELETE).hasAnyAuthority("ROOT","ADMIN")
         .anyRequest().authenticated();
        
    }
   
}
