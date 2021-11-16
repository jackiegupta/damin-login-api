package vn.vme.service;

import java.util.Base64;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import vn.vme.VmeGateApplication;
import vn.vme.common.URI;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=VmeGateApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class OauthTest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Value("${jwt.clientId}")
	private String jwtClientId;
	
	@Value("${jwt.clientSecret}")
	private String jwtClientSecret;

	@Value("${fromUser.email}")
	String testUserEmail;
	@Value("${fromUser.password}")
	String testUserPassword;
	 
	@LocalServerPort
	private int port;
	    
	@Test
	public void testOauthToken() throws Exception {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    String basicToken = jwtClientId+":"+jwtClientSecret;
		headers.add("Authorization", "Basic "+ Base64.getEncoder().encodeToString(basicToken.getBytes()));
	    HttpEntity<String> header = new HttpEntity<String>(headers);
		String url = "http://localhost:" + port + URI.V1 + URI.OAUTH_TOKEN;
		url += "?grant_type=password";
		url += "&username=" + testUserEmail;
		url += "&password=" + testUserPassword;
	    ResponseEntity response = this.restTemplate.postForEntity(url, header, ResponseEntity.class);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());
		Map<String, Object> body = (Map<String, Object>) response.getBody(); 
		String token = (String) body.get("access_token");
		Assert.assertNotNull(token);
		
	}
}
