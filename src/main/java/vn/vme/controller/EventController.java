package vn.vme.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import vn.vme.common.Utils;
import vn.vme.entity.Event;
import vn.vme.entity.EventGame;
import vn.vme.entity.EventPool;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.EventRequest;
import vn.vme.io.game.EventVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.EventRepository;
import vn.vme.service.EventService;
import vn.vme.service.ResultService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.EVENT)
public class EventController extends BaseController {

	static Logger log = LoggerFactory.getLogger(EventController.class.getName());

	@Autowired
	public EventService eventService;
	
	@Autowired
	public EventRepository eventRepository;
	
	@Autowired
	public ResultService resultService;
	
	
	@Autowired(required = false)
	public StorageService storageService;
	
	@Autowired
	Environment env;
	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Create new user")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Event create successfully  ", response = EventVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createEvent(@ModelAttribute EventRequest request) throws Exception {
		log.info("Start createEvent");
		super.validateRequest(request);
		Event event = eventService.create(request);
		if(event != null) {
			event = uploadFile(request, event);
		}
		String pools = request.getPools();
		if(pools != null) {
			String[] poolIds = pools.split(",");
			int lengthPool = poolIds.length;
			if(lengthPool > 0) {
				for(int i = 0; i < lengthPool; i++ ) {
					EventPool eventPool = new EventPool(event.getId(), Integer.valueOf(poolIds[i]));
					eventService.savePool(eventPool);
				}
			}
		}
		String games = request.getGames();
		if(games != null) {
			String[] gameIds = games.split(",");
			int lengthGame = gameIds.length;
			if(lengthGame > 0) {
				for(int i = 0; i < lengthGame; i++ ) {
					EventGame eventGame = new EventGame(event.getId(), Integer.valueOf(gameIds[i]));
					eventService.saveGame(eventGame);
				}
			}
		}
		return response(event.getVO());
		
	}	
	
	@ApiOperation(value = "Update user")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of user update", response = EventVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateEvent(
			@ApiParam(value = "Object json user") @ModelAttribute EventRequest request) throws Exception {
		
		Integer id = request.getId();
		log.info("Update Event:" + request);
		Event existed = eventService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Event Id [" + id + "] invalid ");
		}
		
		Utils.copyNonNullProperties(request, existed);
		existed = eventService.save(existed);
		existed = uploadFile(request, existed);
		
		String pools = request.getPools();
		if(pools != null) {
			String[] poolIds = pools.split(",");
			int lengthPool = poolIds.length;
			if(lengthPool > 0) {
				eventService.deletePool(existed.getId());
				for(int i = 0; i < lengthPool; i++ ) {
					EventPool eventPool = new EventPool(existed.getId(), Integer.valueOf(poolIds[i]));
					eventService.savePool(eventPool);
				}
			}
		}
		String games = request.getGames();
		if(games != null) {
			String[] gameIds = games.split(",");
			int lengthGame = gameIds.length;
			if(lengthGame > 0) {
				eventService.deleteGame(existed.getId());
				for(int i = 0; i < lengthGame; i++ ) {
					EventGame eventGame = new EventGame(existed.getId(), Integer.valueOf(gameIds[i]));
					eventService.saveGame(eventGame);
				}
			}
		}
		log.info("Patch Event with existed = " + existed);
		return response(existed.getVO());
	}
	
	private Event uploadFile(EventRequest request, Event event) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			event = eventService.storePhoto(event,file);
		}
		return event;
	}
	
	
	@ApiOperation(value = "Update user avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of user update", response = EventVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateEventPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json user") @RequestParam(value = "id", required = true) Integer id)
			throws Exception {
		log.info("Upload photo:" + id);
		Event existed = eventService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Event Id [" + id + "] invalid ");
		}

		if (file != null) {
			existed = eventService.storePhoto(existed,file);
		}
		existed = eventService.save(existed);
		log.info("Patch Event with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Get Event by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return user response ", response = EventVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getEvent(
			@ApiParam(value = "Get user by Id", defaultValue = "1") @PathVariable Integer id) throws Exception {
		log.info("Get detail Event");

		Event event = eventService.findOne(id);
		log.info("Get user by getId [" + id + "]");
		if (event != null) {
			EventVO eventVO = eventService.getDetail(event);
			return response(eventVO);
		} else {
			throw new NotFoundException("Event Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "List user by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = EventVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listEvent(
			@RequestParam(required = false, defaultValue = "") String key,
			@RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "") String fromDate,
			@RequestParam(required = false, defaultValue = "") String toDate,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Event");
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		EventRequest request = new EventRequest(key, status, fromDate, toDate);
		Page<Event> result = eventRepository.search(request, paging);
		log.info("Search user total elements [" + result.getTotalElements() + "]");
		List<Event> contentList = result.getContent();
		List<EventVO> responseList = new ArrayList<EventVO>();
		
		contentList.forEach(event -> {
			responseList.add(eventService.getDetail(event));
		});
		
		return responseList(responseList, result);
	}

	@ApiOperation(value = "Delete user")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted user", response = Response.class),
			@ApiResponse(code = 201, message = "STARTED, Already registered also verified with Phone Number and Event name. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteEvent(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Event [" + id + "]");
		Event event = eventService.findOne(id);
		if (event != null) {
			log.info("Delete  Event [" + id + "]");
			eventService.delete(id);
			return responseMessage("Deleted  Event [" + id + "] sucessfully");
		} else {
			throw new NotFoundException("Event Id [" + id + "] invalid ");
		}
	}
	
}
