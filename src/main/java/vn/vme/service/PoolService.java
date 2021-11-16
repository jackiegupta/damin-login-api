package vn.vme.service;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import vn.vme.common.JConstants.PoolType;
import vn.vme.common.JConstants.Status;
import vn.vme.common.Utils;
import vn.vme.entity.Pool;
import vn.vme.io.game.GiftcodeRequest;
import vn.vme.io.game.PoolRequest;
import vn.vme.io.game.PoolVO;
import vn.vme.repository.EventRepository;
import vn.vme.repository.GameRepository;
import vn.vme.repository.PoolRepository;

@Service
public class PoolService {
	private static final Logger log = LoggerFactory.getLogger(PoolService.class);

	@Autowired
	private PoolRepository poolRepository;
	
	//@Autowired(required=false)
    //MessagePublisher messagePublisher;

	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	GiftcodeService giftcodeService;
	
	@Autowired
	StorageService storageService;
	
	@Value("${imageUrl}")
	private String imageUrl;
	
	@Autowired
	RestTemplate restTemplate;
	public PoolService() {
	}
	
	public PoolVO load(PoolVO vo) {
		return vo;
	}
	public Pool findOne(Integer id) {
		return poolRepository.findById(id).get();
	}
	public Page<Pool> search(PoolRequest request, Pageable pageable) {
		
		return poolRepository.search(request, pageable);
	}
	
	public Iterable<Pool> findAll() {
		return poolRepository.findAll();
	}
	
	public Pool generate(Pool pool, String code) {
		int count = 1;
		if(pool.getCount() != null) {
			count = pool.getCount();
		}
		String type = pool.getType();
		GiftcodeRequest codeRequest = new GiftcodeRequest(pool);
		
		PoolType poolType = PoolType.valueOf(type);
		switch (poolType) {
		case SINGLE:
		case MULTI:
			log.debug("================================ Gen Giftcode " + count + " ============= ");
			int result = 0;
			while (result <= count) {
				codeRequest.setCode(Utils.genCode(pool.getSize()));
				try {
					giftcodeService.create(codeRequest);
					++result;
				} catch (Exception e) {
					log.warn("create " + codeRequest);
				}
			}
			log.debug("================================ Result " + (result-1) + " ============= ");
			break;
		case ALL:
			if(Utils.isEmpty(code)) {
				code = Utils.genCode(pool.getSize());
			}
			codeRequest.setCode(code);
			giftcodeService.create(codeRequest);
			break;
		default:
			break;
		}
		pool.setStatus(Status.ACTIVE.name());
		return this.save(pool);
	}
	
	public Pool create(PoolRequest request) {
		request.setStatus(Status.INIT.name());
		Pool pool = new Pool(request);
		pool.setCreateDate(new Date());
/*
		if(request.getEventId()!=null && request.getEventId()>0) {
			pool.setEvent(eventRepository.findById(request.getEventId()).orElse(null));
		}
		if(request.getGameId()!=null && request.getGameId()>0) {
			pool.setGame(gameRepository.findById(request.getGameId()).orElse(null));
		}
		*/
		pool = this.save(pool);
		return this.generate(pool, request.getCode());
	}
	
	public Pool update(PoolRequest request, Pool existed) throws Exception {
		log.debug("update " + request);
		Pool pool = new Pool(request);
		Utils.copyNonNullProperties(pool, existed);
		return save(existed);
	}

	public Pool save(Pool pool) {
		log.debug("save " + pool);
		Integer id = pool.getId();
		log.debug("save " + id);
		return poolRepository.save(pool);
	}
	
	public void delete(Integer id) {
		poolRepository.deleteById(id);
	}
	
	public Pool storePhoto(Pool pool, MultipartFile file) {
		Integer id = pool.getId();
		try {
			String oldFile = pool.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = Pool.class.getSimpleName().toLowerCase();
			storageService.store(file, fileName, folder);
			pool.setPhoto(imageUrl + folder + File.separator + fileName);

			if (Utils.isNotEmpty(oldFile) && !Utils.isTestPhoto(oldFile)) {
				storageService.deleteFile(oldFile);
			}
		} catch (Exception ex) {
			log.warn("failed to delete file:{}", ex);
		}
		return save(pool);
	}
}
