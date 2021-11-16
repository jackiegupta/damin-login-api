package vn.vme.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.JCode;
import vn.vme.common.JConstants.Status;
import vn.vme.common.URI;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Game;
import vn.vme.entity.Task;
import vn.vme.entity.User;
import vn.vme.entity.UserGame;
import vn.vme.entity.UserTask;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.GameRequest;
import vn.vme.io.game.GameVO;
import vn.vme.io.game.UserGameRequest;
import vn.vme.io.game.UserTaskRequest;
import vn.vme.io.rest360.User360;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.GameRepository;
import vn.vme.repository.UserGameRepository;
import vn.vme.repository.UserTaskRepository;
import vn.vme.service.GameService;
import vn.vme.service.StorageService;
import vn.vme.service.TaskService;
import vn.vme.service.TransactionService;
import vn.vme.service.UserService;

@RestController
@RequestMapping(URI.V1 + URI.GAME)
public class GameController extends BaseController {

	static Logger log = LoggerFactory.getLogger(GameController.class.getName());
	@Autowired
	public GameService gameService;
	@Autowired
	public UserService userService;

	@Autowired
	public GameRepository gameRepository;
	
	@Autowired(required = false)
	public TaskService taskService;
	
	@Autowired
    public UserTaskRepository userTaskRepository;
	
	@Autowired
	public TransactionService transactionService;

	@Autowired
	public UserGameRepository userGameRepository;

	@Autowired
	public StorageService storageService;

	@ApiOperation(value = "Register new game")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Game successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Game already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Game already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = GameVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity check(@RequestBody GameRequest request) throws Exception {
		log.info("Game registration: " + request);
		this.validateUser();
		super.validateRequest(request);
		UserVO user = UserContextHolder.getContext().getCurrentUser();
		UserGame existed = userGameRepository.findByUserIdAndGameId(user.getId(), request.getId());
		if (existed != null) {
			return response(JCode.UserCode.USER_NAME_EXISTED);
		} else {
			return response();
		}
	}

	@ApiOperation(value = "Register new game")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Game successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Game already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Game already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = GameVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity registerGame(@RequestBody GameRequest request) throws Exception {
		log.info("Game registration: " + request);
		this.validateUser();
		super.validateRequest(request);
		Game game = gameService.register(request);
		return response(game.getVO());
	}

	@ApiOperation(value = "login game")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Game successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Game already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Game already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = GameVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity loginGame(@RequestBody GameRequest request) throws Exception {
		log.info("Game login: " + request);
		this.validateUser();
		super.validateRequest(request);
		User360 game = gameService.login(request);
		return response(game);
	}

	@ApiOperation(value = "Tranfer gạo vào game 360")
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.TRANSFER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity transferGame(@RequestBody UserGameRequest request) throws Exception {
		log.info("Game TRANSFER: " + request);
		this.validateUser();
		super.validateRequest(request);
		Game game = gameService.transfer(request);
		return response(game.getVO());
	}

	@ApiOperation(value = "Update user profile vào game 360")
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity profileGame(@RequestBody User360 request) throws Exception {
		log.info("Game PROFILE: " + request);
		this.validateUser();
		super.validateRequest(request);
		User360 game = gameService.profile();
		return response(game);
	}

	@ApiOperation(value = "Create new general game")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Game successfully registered ", response = GameVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Game already Registered with Phone Number and Domain name but not yet verify", response = GameVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Game already registered also verified. Go signin.", response = GameVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = GameVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, GameName is used, as in the response", response = GameVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = GameVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createGame(@ModelAttribute GameRequest request) throws Exception {
		log.info("Creating game: " + request);
		Game game = gameService.create(request);
		uploadFile(request, game);
		return response(game.getVO());
	}

	private Game uploadFile(GameRequest request, Game game) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			game = gameService.storePhoto(game, file);
		}
		return game;
	}

	@ApiOperation(value = "Update game")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of game update", response = GameVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateGame(@ApiParam(value = "Object json game") @ModelAttribute GameRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Game:" + request);
		Game existed = gameService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Game Id [" + id + "] invalid ");
		}

		existed = gameService.update(request, existed);
		uploadFile(request, existed);
		log.info("Patch Game with existed = " + existed);
		return response(existed.getVO());
	}

	@ApiOperation(value = "Update game avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of game update", response = GameVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateGamePhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json game") @RequestParam(value = "id", required = true) Integer id)
			throws Exception {
		log.info("Upload photo:" + id);
		Game existed = gameService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Game Id [" + id + "] invalid ");
		}

		if (file != null) {
			existed = gameService.storePhoto(existed, file);
		}
		existed = gameService.save(existed);
		log.info("Patch Game with existed = " + existed);
		return response(existed.getVO());
	}

	@ApiOperation(value = "Get Game by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return game response  ", response = GameVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getGame(@ApiParam(value = "Get game by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get game by id [" + id + "]");
		Game game = gameService.findOne(id);
		if (game != null) {
			UserVO userVO = UserContextHolder.getContext().getCurrentUser();
			User user = userService.findOne(userVO.getId());
			if(user != null) {
				UserGame existed = userGameRepository.findByUserIdAndGameId(userVO.getId(), id);
				if (existed == null) {
					UserGame userGame = new UserGame();
					userGame.setStatus(Status.ACTIVE.name());
					//userGame.setAmount(game.getAmount());
					userGame.setGame(game);
					userGame.setUser(user);
					userGame.setCreateDate(new Date());
					userGame.setUpdateDate(new Date());
					userGameRepository.save(userGame);
				}
				
				try {
		            //TASK PLAYGAME
		            Task task = taskService.findByCode("PLAYGAME");
		            if(task != null) {
		            	UserTaskRequest request = new UserTaskRequest("", user.getId(), 0, 0 ,task.getId(), "", "", "");
		            	Date date = new Date();
		            	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");//05-02-2020
		            	String datetime = formatter.format(date);
		            	request.setUpdateDate(datetime + " 00:00:00");
		            	List<UserTask> list = userTaskRepository.findUserTask(request);
		            	if(list !=null && list.size() >= 5) {
		            		log.info("có TASK PLAYGAME");
		            	}else {
		            		log.info("Chưa có TASK PLAYGAME");
		            		UserTask userTask = new UserTask();
		            		userTask.setUser(user);
		            		userTask.setTask(task);
		            		userTask.setCreateDate(new Date());
		            		userTask.setUpdateDate(new Date());
		            		userTaskRepository.save(userTask);
		            		
		            		if(list.size() == 4) {
		            			transactionService.saveTask(task, user);
		            		}
		            	}
		            }
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
				
			}			
			return response(gameService.load(game.getVO()));
		} else {
			throw new NotFoundException("Game Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "List user game lastest")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = GameVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listUserGame(@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "updateDate") String sortProperty) throws Exception {
		log.info("Search Game");
		UserVO userVO = this.getCurrentUser();
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		UserGameRequest request = new UserGameRequest(userVO.getId(), 0, Status.ACTIVE.name());
		Page<UserGame> result = userGameRepository.search(request, paging);
		log.info("Search game total elements [" + result.getTotalElements() + "]");
		List<UserGame> contentList = result.getContent();
		List<GameVO> responseList = new ArrayList<GameVO>();

		for (UserGame userGame : contentList) {
			responseList.add(gameService.load(userGame.getGame().getVO()));
		}
		return responseList(responseList, result);
	}

	@ApiOperation(value = "List game by conditions")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = GameVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listGame(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String name,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Integer gameTypeId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Integer publishId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Boolean news,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Boolean hot,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Boolean h5sdk,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Boolean top,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String platform,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Game");

		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		GameRequest request = new GameRequest(name, gameTypeId, publishId, status, news, hot, top);
		request.setPlatform(platform);
		request.setH5sdk(h5sdk);
		Page<Game> result = gameRepository.search(request, paging);
		log.info("Search game total elements [" + result.getTotalElements() + "]");
		List<Game> contentList = result.getContent();
		List<GameVO> responseList = new ArrayList<GameVO>();

		for (Game game : contentList) {
			responseList.add(gameService.load(game.getVO()));
		}
		return responseList(responseList, result);
	}

	@ApiOperation(value = "Delete game")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted game", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Game fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteGame(
			@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Game [" + id + "]");
		Game game = gameService.findOne(id);
		if (game != null) {
			log.info("Delete  Game [" + game.getId() + "]");
			gameService.delete(game.getId());
			return responseMessage("Deleted  Game [" + game.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Game id [" + id + "] invalid ");
		}
	}

}
