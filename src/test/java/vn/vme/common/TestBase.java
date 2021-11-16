package vn.vme.common;

import java.io.InputStream;
import java.util.Date;

import org.junit.Before;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.vme.common.JConstants.Status;
import vn.vme.entity.User;

/**
 * @m-tech
 */
public class TestBase extends Dummy {
	protected User U00 = null;
	protected User DUMMY = null;
	// Test info dummy
	protected long USER_ID = 1;
	

	@Before
	public void setUp() throws Exception {
		log.info("set Up test env ["+ env.getActiveProfiles() + "] " + url);
		U00 = (User) parserDummy(new User());
	}

	public Object parserDummy(Object object) throws Exception{
		ObjectMapper om = new ObjectMapper();
		String jsonName = null;
		InputStream inputStream = null;
		if(object instanceof User) {
			jsonName = User.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" +jsonName + ".json");
			User parser = om.readValue(inputStream, User.class);
			parser.setStatus(Status.ACTIVE.name());
			parser.setCreateDate(new Date());
			return parser;
		}
		return null;
	}
}
