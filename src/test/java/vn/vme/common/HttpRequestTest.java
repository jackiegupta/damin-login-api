package vn.vme.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import vn.vme.VmeGateApplication;

@RunWith(SpringRunner.class)

@SpringBootTest(classes=VmeGateApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void greetingShouldReturnDefaultMessage() throws Exception {
		ResponseEntity rest = this.restTemplate.getForObject("http://localhost:" + port + "/user", ResponseEntity.class);
		Assert.assertEquals(HttpStatus.OK, rest.getStatusCode());
	}
	@Test
	public void genPassword() throws Exception {
		System.out.println(new BCryptPasswordEncoder().encode("Pass!234"));
	}
}
