package vn.vme.controller;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.URI;
import vn.vme.entity.Pool;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.PoolRequest;
import vn.vme.io.game.PoolVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.PoolRepository;
import vn.vme.service.GameService;
import vn.vme.service.PoolService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.POOL)
public class PoolController extends BaseController {

	static Logger log = LoggerFactory.getLogger(PoolController.class.getName());
	@Autowired
	public PoolService poolService;
	
	@Autowired
	public GameService gameService;
	
	@Autowired
	public PoolRepository poolRepository;
	
	
	@Autowired
	public StorageService storageService;

	@ApiOperation(value = "Register new pool")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Pool successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Pool already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Pool already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = PoolVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.GENERATE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity generatePool(@RequestBody PoolRequest request)
			throws Exception {
		log.info("Pool registration: " + request);
		super.validateRequest(request);
		Pool pool = poolService.create(request);
		poolService.generate(pool, request.getCode());
		return response(pool.getVO());
	}
	
	@ApiOperation(value = "Create new general pool")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Pool successfully registered ", response = PoolVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Pool already Registered with Phone Number and Domain name but not yet verify", response = PoolVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Pool already registered also verified. Go signin.", response = PoolVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = PoolVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, PoolName is used, as in the response", response = PoolVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = PoolVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createPool(@ModelAttribute PoolRequest request)
			throws Exception {
		log.info("Creating pool: " + request);
		Pool pool = poolService.create(request);
		uploadFile(request, pool);
		return response(pool.getVO());
	}
	
	@ApiOperation(value = "Update pool")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of pool update", response = PoolVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updatePool(@ApiParam(value = "Object json pool") @ModelAttribute PoolRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Pool:" + request);
		Pool existed = poolService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Pool Id [" + id + "] invalid ");
		}
		
		existed = poolService.update(request, existed);
		uploadFile(request, existed);
		log.info("Patch Pool with existed = " + existed);
		return response(existed.getVO());
	}
	
	private Pool uploadFile(PoolRequest request, Pool pool) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			pool = poolService.storePhoto(pool,file);
		}
		return pool;
	}
	

	@ApiOperation(value = "Get Pool by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return pool response  ", response = PoolVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getPool(@ApiParam(value = "Get pool by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get pool by id [" + id + "]");
		Pool pool = poolService.findOne(id);
		if (pool != null) {
			return response(poolService.load(pool.getVO()));
		} else {
			throw new NotFoundException("Pool Id [" + id + "] invalid ");
		}
	}

	
	@ApiOperation(value = "List pool by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = PoolVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('USER','OP','ADMIN')")
	public ResponseEntity listPool(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String scope,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") int gameId,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Pool");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		PoolRequest request = new PoolRequest(type,scope, status);
		request.setGameId(gameId);
		Page<Pool> result = poolRepository.search(request, paging);
		log.info("Search pool total elements [" + result.getTotalElements() + "]");
		List<Pool> contentList = result.getContent();
		List<PoolVO> responseList = new ArrayList<PoolVO>();

		for (Pool pool : contentList) {
			responseList.add(poolService.load(pool.getVO()));
		}
		return responseList(responseList, result);
	}
	/*
	@ApiOperation(value = "List pool by conditions")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = PoolVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.GAME, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('USER','OP','ADMIN')")
	public ResponseEntity listPoolGame(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String gameName,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String scope,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Pool GAME");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		PoolRequest request = new PoolRequest(type,scope, status, gameName);
		Page<Game> result = poolRepository.searchByGame(request, paging);
		log.info("Search pool total elements [" + result.getTotalElements() + "]");
		List<Game> contentList = result.getContent();
		List<GameVO> responseList = new ArrayList<GameVO>();
		
		for (Game pool : contentList) {
			responseList.add(gameService.load(pool.getVO()));
		}
		return responseList(responseList, result);
	}
*/
	
	@ApiOperation(value = "Delete pool")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted pool", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Pool fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deletePool(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Pool [" + id + "]");
		Pool pool = poolService.findOne(id);
		if (pool != null) {
			log.info("Delete  Pool [" + pool.getId() + "]");
			poolService.delete(pool.getId());
			return responseMessage("Deleted  Pool [" + pool.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Pool id [" + id + "] invalid ");
		}
	}

	
}
