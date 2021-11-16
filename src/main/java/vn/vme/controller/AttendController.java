package vn.vme.controller;
import java.util.ArrayList;
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
import vn.vme.entity.Attend;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.AttendRequest;
import vn.vme.io.game.AttendVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.AttendRepository;
import vn.vme.service.AttendService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.ATTEND)
public class AttendController extends BaseController {

	static Logger log = LoggerFactory.getLogger(AttendController.class.getName());
	@Autowired
	public AttendService attendService;
	
	@Autowired
	public AttendRepository attendRepository;
	@Autowired
	public StorageService storageService;
	
	@ApiOperation(value = "Create new general attend")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Attend successfully registered ", response = AttendVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Attend already Registered with Phone Number and Domain name but not yet verify", response = AttendVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Attend already registered also verified. Go signin.", response = AttendVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = AttendVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, AttendName is used, as in the response", response = AttendVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = AttendVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createAttend(@RequestBody AttendRequest request)
			throws Exception {
		log.info("Creating attend: " + request);
		Attend attend = attendService.create(request);
		return response(attend.getVO());
	}
	
	@ApiOperation(value = "Update attend")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of attend update", response = AttendVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateAttend(@ApiParam(value = "Object json attend") @RequestBody AttendRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Attend:" + request);
		Attend existed = attendService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Attend Id [" + id + "] invalid ");
		}
		
		existed = attendService.update(request, existed);
		log.info("Patch Attend with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Get Attend by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return attend response  ", response = AttendVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getAttend(@ApiParam(value = "Get attend by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get attend by id [" + id + "]");
		Attend attend = attendService.findOne(id);
		if (attend != null) {
			return response(attendService.load(attend.getVO()));
		} else {
			throw new NotFoundException("Attend Id [" + id + "] invalid ");
		}
	}
	@ApiOperation(value = "List attend by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = AttendVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listAttend(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Attend");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		AttendRequest request = new AttendRequest(key,status);
		Page<Attend> result = attendRepository.search(request, paging);
		log.info("Search attend total elements [" + result.getTotalElements() + "]");
		List<Attend> contentList = result.getContent();
		List<AttendVO> responseList = new ArrayList<AttendVO>();

		for (Attend attend : contentList) {
			responseList.add(attendService.load(attend.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete attend")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted attend", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Attend fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteAttend(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Attend [" + id + "]");
		Attend attend = attendService.findOne(id);
		if (attend != null) {
			log.info("Delete  Attend [" + attend.getId() + "]");
			attendService.delete(attend.getId());
			return responseMessage("Deleted  Attend [" + attend.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Attend id [" + id + "] invalid ");
		}
	}

	
}
