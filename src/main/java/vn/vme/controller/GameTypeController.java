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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.URI;
import vn.vme.entity.GameType;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.GameTypeRequest;
import vn.vme.io.game.GameTypeVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.GameTypeRepository;
import vn.vme.service.GameTypeService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.GAME_TYPE)
public class GameTypeController extends BaseController {

	static Logger log = LoggerFactory.getLogger(GameTypeController.class.getName());
	@Autowired
	public GameTypeService gameTypeService;
	
	@Autowired
	public GameTypeRepository gameTypeRepository;
	
	@Autowired
	public StorageService storageService;
	
	@ApiOperation(value = "Create new general gameType")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, GameType successfully registered ", response = GameTypeVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, GameType already Registered with Phone Number and Domain name but not yet verify", response = GameTypeVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, GameType already registered also verified. Go signin.", response = GameTypeVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = GameTypeVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, GameTypeName is used, as in the response", response = GameTypeVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = GameTypeVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createGameType(@ModelAttribute GameTypeRequest request)
			throws Exception {
		log.info("Creating gameType: " + request);
		GameType gameType = gameTypeService.create(request);
		uploadFile(request, gameType);
		return response(gameType.getVO());
	}
	
	@ApiOperation(value = "Update gameType")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of gameType update", response = GameTypeVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateGameType(@ApiParam(value = "Object json gameType") @ModelAttribute GameTypeRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update GameType:" + request);
		GameType existed = gameTypeService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("GameType Id [" + id + "] invalid ");
		}
		
		existed = gameTypeService.update(request, existed);
		uploadFile(request, existed);
		log.info("Patch GameType with existed = " + existed);
		return response(existed.getVO());
	}
	
	private GameType uploadFile(GameTypeRequest request, GameType game) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			game = gameTypeService.storePhoto(game,file);
		}
		return game;
	}
	
	@ApiOperation(value = "Update gameType avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of gameType update", response = GameTypeVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateGameTypePhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json gameType") @RequestParam(value = "id", required = true) Integer id)
			throws Exception {
		log.info("Upload photo:" + id);
		GameType existed = gameTypeService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("GameType Id [" + id + "] invalid ");
		}

		if (file != null) {
			existed = gameTypeService.storePhoto(existed,file);
		}
		existed = gameTypeService.save(existed);
		log.info("Patch GameType with existed = " + existed);
		return response(existed.getVO());
	}

	@ApiOperation(value = "Get GameType by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return gameType response  ", response = GameTypeVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getGameType(@ApiParam(value = "Get gameType by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get gameType by id [" + id + "]");
		GameType gameType = gameTypeService.findOne(id);
		if (gameType != null) {
			return response(gameTypeService.load(gameType.getVO()));
		} else {
			throw new NotFoundException("GameType Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "List gameType by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = GameTypeVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listGameType(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search GameType");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		GameTypeRequest request = new GameTypeRequest(key, type, status);
		Page<GameType> result = gameTypeRepository.search(request, paging);
		log.info("Search gameType total elements [" + result.getTotalElements() + "]");
		List<GameType> contentList = result.getContent();
		List<GameTypeVO> responseList = new ArrayList<GameTypeVO>();

		for (GameType gameType : contentList) {
			responseList.add(gameTypeService.load(gameType.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete gameType")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted gameType", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and GameType fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteGameType(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  GameType [" + id + "]");
		GameType gameType = gameTypeService.findOne(id);
		if (gameType != null) {
			log.info("Delete  GameType [" + gameType.getId() + "]");
			gameTypeService.delete(gameType.getId());
			return responseMessage("Deleted  GameType [" + gameType.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("GameType id [" + id + "] invalid ");
		}
	}

	
}
