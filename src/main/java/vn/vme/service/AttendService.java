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
import vn.vme.entity.Attend;
import vn.vme.io.game.AttendRequest;
import vn.vme.io.game.AttendVO;
import vn.vme.repository.AttendRepository;

@Service
public class AttendService {
	private static final Logger log = LoggerFactory.getLogger(AttendService.class);

	@Autowired
	private AttendRepository attendRepository;
	
	//@Autowired(required=false)
    //MessagePublisher messagePublisher;

	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Value("${imageUrl}")
	private String imageUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RestTemplate restTemplate;
	
	
	public AttendService() {
	}
	
	public AttendVO load(AttendVO vo) {
		return vo;
	}
	public Attend findOne(Integer id) {
		return attendRepository.findById(id).get();
	}
	public Page<Attend> search(AttendRequest request, Pageable pageable) {
		
		return attendRepository.search(request, pageable);
	}
	
	public Iterable<Attend> findAll() {
		return attendRepository.findAll();
	}
	public Attend create(AttendRequest request) {
		request.setStatus(Status.INACTIVE.name());
		Attend attend = new Attend(request);
		attend.setCreateDate(new Date());
		attend.setUpdateDate(new Date());
		this.save(attend);
		return attend;
	}
	
	public Attend update(AttendRequest request, Attend existed) throws Exception {
		log.debug("update " + request);
		Attend attend = new Attend(request);
		Utils.copyNonNullProperties(attend, existed);
		existed = save(existed);
		return existed;
	}

	public Attend save(Attend attend) {
		log.debug("save " + attend);
		Integer id = attend.getId();
		attend = attendRepository.save(attend);
		log.debug("save " + id);
		return attend;
	}
	
	public void delete(Integer id) {
		attendRepository.deleteById(id);
	}

}
