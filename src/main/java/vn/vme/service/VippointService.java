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
import vn.vme.entity.Vippoint;
import vn.vme.io.game.VippointRequest;
import vn.vme.io.game.VippointVO;
import vn.vme.repository.VippointRepository;

@Service
public class VippointService {
	private static final Logger log = LoggerFactory.getLogger(VippointService.class);

	@Autowired
	private VippointRepository vippointRepository;
	
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
	
	
	public VippointService() {
	}
	
	public VippointVO load(VippointVO vo) {
		return vo;
	}
	public Vippoint findOne(Integer id) {
		return vippointRepository.findById(id).get();
	}
	public Page<Vippoint> search(VippointRequest request, Pageable pageable) {
		
		return vippointRepository.search(request, pageable);
	}
	
	public Iterable<Vippoint> findAll() {
		return vippointRepository.findAll();
	}
	public Vippoint create(VippointRequest request) {
		request.setStatus(Status.INACTIVE.name());
		Vippoint vippoint = new Vippoint(request);
		vippoint.setCreateDate(new Date());
		vippoint.setUpdateDate(new Date());
		this.save(vippoint);
		return vippoint;
	}
	
	public Vippoint update(VippointRequest request, Vippoint existed) throws Exception {
		log.debug("update " + request);
		Vippoint vippoint = new Vippoint(request);
		Utils.copyNonNullProperties(vippoint, existed);
		existed = save(existed);
		return existed;
	}

	public Vippoint save(Vippoint vippoint) {
		log.debug("save " + vippoint);
		Integer id = vippoint.getId();
		vippoint = vippointRepository.save(vippoint);
		log.debug("save " + id);
		return vippoint;
	}
	
	public void delete(Integer id) {
		vippointRepository.deleteById(id);
	}

}
