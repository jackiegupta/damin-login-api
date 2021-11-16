package vn.vme.service;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import vn.vme.VmeGateApplication;
import vn.vme.common.JConstants;
import vn.vme.common.JConstants.Status;
import vn.vme.common.TestBase;
import vn.vme.controller.UserController;
import vn.vme.exception.ExistedEmailException;
import vn.vme.exception.ExistedNameException;
import vn.vme.exception.ExistedPhoneException;
import vn.vme.io.user.UserRequest;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VmeGateApplication.class)
public class UserSequenceTest extends TestBase {

	@Autowired
	private UserController controller;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testServiceUserCRUD() throws Exception {

		UserRequest request = U00.getRequest();
		ResponseEntity entity = null;
		Long id = 0L;
		UserVO response = null;
		try {
			entity = controller.createUser(request);//1.// t
			response = (UserVO) entity.getBody();
			id = response.getId();
			Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());
			Assert.assertNotNull(response);

			// Email already conflicted
			try {
				entity = controller.createUser(request);
			} catch (ExistedEmailException e) {
				assertNotNull(e.getMessage());
			}

			// User name already conflicted
			request.setEmail("test" + request.getEmail());
			try {
				entity = controller.createUser(request);
			} catch (ExistedNameException e) {
				assertNotNull(e.getMessage());
			}
			
			// Phone already conflicted
			request.setUserName("test" + request.getUserName());
			try {
				entity = controller.createUser(request);
			} catch (ExistedPhoneException e) {
				assertNotNull(e.getMessage());
			}

			// Update entire object with PUT method
			String updateStatus = Status.INACTIVE.name();
			request = new UserRequest();
			request.setId(id);
			request.setStatus(updateStatus);
			entity = controller.updateUser(request);
			Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());

			// Get User
			entity = controller.getUser(id);
			Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());
			response = (UserVO) entity.getBody();
			Assert.assertEquals(updateStatus, response.getStatus());

			// Search List
			entity = controller.listUser( null, null,null, Paging.PAGE, Paging.SIZE, Paging.DESC, Paging.SORT);
			Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());
			Map<String, Object> map = (HashMap<String, Object>) entity.getBody();
			Paging paging = (Paging) map.get(Paging.class.getSimpleName().toLowerCase());
			Assert.assertNotNull(paging);
			Assert.assertTrue(paging.getTotalRows() >= 1);
			List<UserVO> list = (List<UserVO>) map.get(JConstants.DATA_LIST);
			UserVO user = list.get(0);
			Assert.assertTrue(id.compareTo(user.getId()) == 0);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} finally {
			// Clean if existed
			if (id > 0) {
				controller.deleteUser(""+id);
			}
		}
	}
	
	@Test
	public void testSearchUser() throws Exception {
		// Search List
		ResponseEntity entity = null;
		entity = controller.listUser( null, null, null,Paging.PAGE, Paging.SIZE, Paging.DESC, Paging.SORT);
	}
}
