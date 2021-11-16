package vn.vme.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.vme.common.JConstants.Status;
import vn.vme.common.Utils;
import vn.vme.entity.Task;
import vn.vme.io.game.TaskRequest;
import vn.vme.io.game.TaskVO;
import vn.vme.repository.EventRepository;
import vn.vme.repository.TaskRepository;

@Service
public class TaskService {
	private static final Logger log = LoggerFactory.getLogger(TaskService.class);

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private EventRepository eventRepository;

	//@Autowired(required=false)
    //MessagePublisher messagePublisher;

	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RestTemplate restTemplate;
	public TaskService() {
	}
	
	public TaskVO load(TaskVO vo) {
		return vo;
	}
	public Task findOne(Integer id) {
		return taskRepository.findById(id).get();
	}
	public Page<Task> search(TaskRequest request, Pageable pageable) {
		
		return taskRepository.search(request, pageable);
	}
	
	public Iterable<Task> findAll() {
		return taskRepository.findAll();
	}
	public Task create(TaskRequest request) {
		//request.setStatus(Status.INACTIVE.name());
		Task task = new Task(request);
		task.setCreateDate(new Date());
		return this.save(task);
	}
	
	public Task update(TaskRequest request, Task existed) throws Exception {
		log.debug("update " + request);
		Task task = new Task(request);
		Utils.copyNonNullProperties(task, existed);
		return save(existed);
	}

	public Task save(Task task) {
		log.debug("save " + task);
		Integer id = task.getId();
		log.debug("save " + id);
		return taskRepository.save(task);
	}
	
	public void delete(Integer id) {
		taskRepository.deleteById(id);
	}

	public Task findByCode(String code) {
		return taskRepository.findByCode(code);
	}
}
