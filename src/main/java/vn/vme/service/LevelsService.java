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
import vn.vme.entity.Levels;
import vn.vme.io.game.LevelsRequest;
import vn.vme.io.game.LevelsVO;
import vn.vme.repository.LevelsRepository;

@Service
public class LevelsService {
	private static final Logger log = LoggerFactory.getLogger(LevelsService.class);

	@Autowired
	private LevelsRepository levelsRepository;
	
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
	
	
	public LevelsService() {
	}
	
	public LevelsVO load(LevelsVO vo) {
		return vo;
	}
	public Levels findOne(Integer id) {
		return levelsRepository.findById(id).get();
	}
	public Page<Levels> search(LevelsRequest request, Pageable pageable) {
		
		return levelsRepository.search(request, pageable);
	}
	
	public Iterable<Levels> findAll() {
		return levelsRepository.findAll();
	}
	public Levels create(LevelsRequest request) {
		request.setStatus(Status.INACTIVE.name());
		Levels levels = new Levels(request);
		levels.setCreateDate(new Date());
		levels.setUpdateDate(new Date());
		this.save(levels);
		return levels;
	}
	
	public Levels update(LevelsRequest request, Levels existed) throws Exception {
		log.debug("update " + request);
		Levels levels = new Levels(request);
		Utils.copyNonNullProperties(levels, existed);
		existed = save(existed);
		return existed;
	}

	public Levels save(Levels levels) {
		log.debug("save " + levels);
		Integer id = levels.getId();
		levels = levelsRepository.save(levels);
		log.debug("save " + id);
		return levels;
	}
	
	public void delete(Integer id) {
		levelsRepository.deleteById(id);
	}

}
