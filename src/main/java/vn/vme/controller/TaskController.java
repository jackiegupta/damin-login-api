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
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Task;
import vn.vme.entity.UserTask;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.TaskRequest;
import vn.vme.io.game.TaskVO;
import vn.vme.io.game.UserTaskRequest;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.TaskRepository;
import vn.vme.repository.UserTaskRepository;
import vn.vme.service.StorageService;
import vn.vme.service.TaskService;

@RestController
@RequestMapping(URI.V1 + URI.TASK)
public class TaskController extends BaseController {

	static Logger log = LoggerFactory.getLogger(TaskController.class.getName());
	@Autowired
	public TaskService taskService;
	
	@Autowired
	public TaskRepository taskRepository;
	
	@Autowired
	public UserTaskRepository userTaskRepository;
	
	@Autowired
	public StorageService storageService;

	@ApiOperation(value = "Register new task")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Task successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Task already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Task already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = TaskVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity registerTask(@RequestBody TaskRequest request)
			throws Exception {
		log.info("Task registration: " + request);
		super.validateRequest(request);
		Task task = taskService.create(request);
		return response(task.getVO());
	}
	
	@ApiOperation(value = "Create new general task")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Task successfully registered ", response = TaskVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Task already Registered with Phone Number and Domain name but not yet verify", response = TaskVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Task already registered also verified. Go signin.", response = TaskVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = TaskVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, TaskName is used, as in the response", response = TaskVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = TaskVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity createTask(@RequestBody TaskRequest request)
			throws Exception {
		log.info("Creating task: " + request);
		Task task = taskService.create(request);
		return response(task.getVO());
	}
	
	@ApiOperation(value = "Update task")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of task update", response = TaskVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity updateTask(@ApiParam(value = "Object json task") @RequestBody TaskRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Task:" + request);
		Task existed = taskService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Task Id [" + id + "] invalid ");
		}
		
		existed = taskService.update(request, existed);
		log.info("Patch Task with existed = " + existed);
		return response(existed.getVO());
	}

	@ApiOperation(value = "Get Task by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return task response  ", response = TaskVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getTask(@ApiParam(value = "Get task by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get task by id [" + id + "]");
		Task task = taskService.findOne(id);
		if (task != null) {
			return response(taskService.load(task.getVO()));
		} else {
			throw new NotFoundException("Task Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "Get Task by user id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return task response  ", response = TaskVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listUserTask(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Integer poolId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Integer eventId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Integer taskId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		this.validateUser();
		Long userId = 0L;
		UserVO user = UserContextHolder.getContext().getCurrentUser();
		if(user!=null) {
			userId = user.getId();
		}
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		UserTaskRequest request = new UserTaskRequest(key, userId, poolId, eventId,taskId, status, "", "");
		Page<UserTask> result = userTaskRepository.search(request, paging);
		log.info("Search task total elements [" + result.getTotalElements() + "]");
		List<UserTask> contentList = result.getContent();
		List<TaskVO> responseList = new ArrayList<TaskVO>();

		for (UserTask userTask : contentList) {
			responseList.add(taskService.load(userTask.getTask().getVO()));
		}
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "List task by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = TaskVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listTask(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Integer eventId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Long userId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Task");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		TaskRequest request = new TaskRequest(key,userId, eventId, status);
		Page<Task> result = taskRepository.search(request, paging);
		log.info("Search task total elements [" + result.getTotalElements() + "]");
		List<Task> contentList = result.getContent();
		List<TaskVO> responseList = new ArrayList<TaskVO>();

		for (Task task : contentList) {
			responseList.add(taskService.load(task.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete task")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted task", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Task fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteTask(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Task [" + id + "]");
		Task task = taskService.findOne(id);
		if (task != null) {
			log.info("Delete  Task [" + task.getId() + "]");
			taskService.delete(task.getId());
			return responseMessage("Deleted  Task [" + task.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Task id [" + id + "] invalid ");
		}
	}

	
}
