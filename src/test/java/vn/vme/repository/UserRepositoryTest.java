package vn.vme.repository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import vn.vme.common.JConstants.Status;
import vn.vme.common.TestBase;
import vn.vme.entity.User;
import vn.vme.model.Paging;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest extends TestBase {

	@Autowired
	protected UserRepository userRepository;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void destroy() throws Exception {
	}

	@Test
	public void testCRUDUser() {
		
		Long id = 0L;
		try {
			// Create
			U00 = userRepository.save(U00);

			id = U00.getId();
			Assert.assertTrue(id > 0);

			// Read User by Id
			User user = userRepository.findById(id).get();

			Assert.assertNotNull(user);
			Assert.assertEquals(id, user.getId());
			Assert.assertEquals(U00.getStatus(), user.getStatus());
			String userName = U00.getUserName();
			String email = U00.getEmail();
			String phone = U00.getPhone();
			// Read User by email
			user = userRepository.findByUserName(userName);
			Assert.assertNotNull(user);
			Assert.assertEquals(id, user.getId());
			Assert.assertEquals(userName, user.getUserName());
			
			// Read User by email
			user = userRepository.findByEmail(email);
			Assert.assertNotNull(user);
			Assert.assertEquals(id, user.getId());
			Assert.assertEquals(email, user.getEmail());

			// Update
			String updateStatus = Status.INACTIVE.name();
			U00.setStatus(updateStatus);
			U00 = userRepository.save(U00);
			Assert.assertEquals(updateStatus, U00.getStatus());
			
			User userEmail = userRepository.findByUserNameOrEmailOrPhone(userName, email,phone);
			Assert.assertNotNull(userEmail);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			userRepository.deleteById(id);
			Assert.assertNull(userRepository.findById(id));
		}
	}
	
	@Test
	public void testListUser() {
		try {
			Pageable paging = PageRequest.of(Paging.PAGE - 1, Paging.SIZE);
			Page<User> list = userRepository.findAll(paging);
			log.debug("Test find all, found [" + list.getTotalElements() + "]");
			User user = list.getContent().get(0);
			log.debug("User [" + user + "]");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}
	@Test
	public void testRoleUser() {
		try {
			Pageable paging = PageRequest.of(Paging.PAGE - 1, Paging.SIZE);
			Page<User> list = userRepository.findAll(paging);
			log.debug("Test find all, found [" + list.getTotalElements() + "]");
			User user = list.getContent().get(0);
			log.debug("User [" + user + "]");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}
}
