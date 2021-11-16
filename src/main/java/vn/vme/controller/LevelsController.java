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
import vn.vme.entity.Levels;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.LevelsRequest;
import vn.vme.io.game.LevelsVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.LevelsRepository;
import vn.vme.service.LevelsService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.LEVELS)
public class LevelsController extends BaseController {

	static Logger log = LoggerFactory.getLogger(LevelsController.class.getName());
	@Autowired
	public LevelsService levelsService;
	
	@Autowired
	public LevelsRepository levelsRepository;
	@Autowired
	public StorageService storageService;
	
	@ApiOperation(value = "Create new general levels")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Levels successfully registered ", response = LevelsVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Levels already Registered with Phone Number and Domain name but not yet verify", response = LevelsVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Levels already registered also verified. Go signin.", response = LevelsVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = LevelsVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, LevelsName is used, as in the response", response = LevelsVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = LevelsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity createLevels(@RequestBody LevelsRequest request)
			throws Exception {
		log.info("Creating levels: " + request);
		Levels levels = levelsService.create(request);
		return response(levels.getVO());
	}
	
	@ApiOperation(value = "Update levels")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of levels update", response = LevelsVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity updateLevels(@ApiParam(value = "Object json levels") @RequestBody LevelsRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Levels:" + request);
		Levels existed = levelsService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Levels Id [" + id + "] invalid ");
		}
		
		existed = levelsService.update(request, existed);
		log.info("Patch Levels with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Get Levels by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return levels response  ", response = LevelsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getLevels(@ApiParam(value = "Get levels by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get levels by id [" + id + "]");
		Levels levels = levelsService.findOne(id);
		if (levels != null) {
			return response(levelsService.load(levels.getVO()));
		} else {
			throw new NotFoundException("Levels Id [" + id + "] invalid ");
		}
	}
	@ApiOperation(value = "List levels by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = LevelsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listLevels(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Levels");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		LevelsRequest request = new LevelsRequest(key,status);
		Page<Levels> result = levelsRepository.search(request, paging);
		log.info("Search levels total elements [" + result.getTotalElements() + "]");
		List<Levels> contentList = result.getContent();
		List<LevelsVO> responseList = new ArrayList<LevelsVO>();

		for (Levels levels : contentList) {
			responseList.add(levelsService.load(levels.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete levels")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted levels", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Levels fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity deleteLevels(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Levels [" + id + "]");
		Levels levels = levelsService.findOne(id);
		if (levels != null) {
			log.info("Delete  Levels [" + levels.getId() + "]");
			levelsService.delete(levels.getId());
			return responseMessage("Deleted  Levels [" + levels.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Levels id [" + id + "] invalid ");
		}
	}

	
}
