package vn.vme.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.URI;
import vn.vme.entity.User;
import vn.vme.exception.NotFoundException;
import vn.vme.io.user.CmsVO;
import vn.vme.io.user.UserRequest;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.UserRepository;
import vn.vme.service.StorageService;
import vn.vme.service.UserService;

@RestController
@RequestMapping(URI.V1 + URI.CMS)
public class CmsController extends BaseController {

	static Logger log = LoggerFactory.getLogger(CmsController.class.getName());
	@Autowired
	public UserService userService;
	
	@Autowired
	public UserRepository userRepository;
	@Autowired
	public StorageService storageService;
	
	
	@PreAuthorize ("hasRole('ADMIN')")
	@GetMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity countNewUser() throws Exception {
		log.info("countNewUser");
		  Date currentDate=java.util.Calendar.getInstance().getTime();  
		 long userCount = userService.countNewUser(currentDate); 
		 CmsVO cms = new CmsVO(userCount);
		 return response(cms);
	}
	
}
