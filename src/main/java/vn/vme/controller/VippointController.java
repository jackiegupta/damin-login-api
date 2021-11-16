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
import vn.vme.entity.Vippoint;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.VippointRequest;
import vn.vme.io.game.VippointVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.VippointRepository;
import vn.vme.service.VippointService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.VIPPOINT)
public class VippointController extends BaseController {

	static Logger log = LoggerFactory.getLogger(VippointController.class.getName());
	@Autowired
	public VippointService vippointService;
	
	@Autowired
	public VippointRepository vippointRepository;
	@Autowired
	public StorageService storageService;
	
	@ApiOperation(value = "Create new general vippoint")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Vippoint successfully registered ", response = VippointVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Vippoint already Registered with Phone Number and Domain name but not yet verify", response = VippointVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Vippoint already registered also verified. Go signin.", response = VippointVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = VippointVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, VippointName is used, as in the response", response = VippointVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = VippointVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createVippoint(@RequestBody VippointRequest request)
			throws Exception {
		log.info("Creating vippoint: " + request);
		Vippoint vippoint = vippointService.create(request);
		return response(vippoint.getVO());
	}
	
	@ApiOperation(value = "Update vippoint")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of vippoint update", response = VippointVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updateVippoint(@ApiParam(value = "Object json vippoint") @RequestBody VippointRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Vippoint:" + request);
		Vippoint existed = vippointService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Vippoint Id [" + id + "] invalid ");
		}
		
		existed = vippointService.update(request, existed);
		log.info("Patch Vippoint with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Get Vippoint by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return vippoint response  ", response = VippointVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getVippoint(@ApiParam(value = "Get vippoint by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get vippoint by id [" + id + "]");
		Vippoint vippoint = vippointService.findOne(id);
		if (vippoint != null) {
			return response(vippointService.load(vippoint.getVO()));
		} else {
			throw new NotFoundException("Vippoint Id [" + id + "] invalid ");
		}
	}
	@ApiOperation(value = "List vippoint by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = VippointVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listVippoint(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Vippoint");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		VippointRequest request = new VippointRequest(key,status);
		Page<Vippoint> result = vippointRepository.search(request, paging);
		log.info("Search vippoint total elements [" + result.getTotalElements() + "]");
		List<Vippoint> contentList = result.getContent();
		List<VippointVO> responseList = new ArrayList<VippointVO>();

		for (Vippoint vippoint : contentList) {
			responseList.add(vippointService.load(vippoint.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete vippoint")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted vippoint", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Vippoint fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteVippoint(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Vippoint [" + id + "]");
		Vippoint vippoint = vippointService.findOne(id);
		if (vippoint != null) {
			log.info("Delete  Vippoint [" + vippoint.getId() + "]");
			vippointService.delete(vippoint.getId());
			return responseMessage("Deleted  Vippoint [" + vippoint.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Vippoint id [" + id + "] invalid ");
		}
	}

	
}
