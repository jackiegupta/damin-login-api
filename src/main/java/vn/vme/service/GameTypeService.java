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

import vn.vme.common.JConstants.Status;
import vn.vme.common.Utils;
import vn.vme.entity.GameType;
import vn.vme.io.game.GameTypeRequest;
import vn.vme.io.game.GameTypeVO;
import vn.vme.repository.GameTypeRepository;

@Service
public class GameTypeService {
	private static final Logger log = LoggerFactory.getLogger(GameTypeService.class);

	@Autowired
	private GameTypeRepository gameTypeRepository;

	//@Autowired(required=false)
    //MessagePublisher messagePublisher;

	@Value("${imageUrl}")
	private String imageUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	RestTemplate restTemplate;
	public GameTypeService() {
	}
	
	public GameTypeVO load(GameTypeVO vo) {
		return vo;
	}
	public GameType findOne(Integer id) {
		return gameTypeRepository.findById(id).get();
	}
	public Page<GameType> search(GameTypeRequest request, Pageable pageable) {
		
		return gameTypeRepository.search(request, pageable);
	}
	
	public Iterable<GameType> findAll() {
		return gameTypeRepository.findAll();
	}
	public GameType create(GameTypeRequest request) {
		request.setStatus(Status.ACTIVE.name());
		GameType gameType = new GameType(request);
		gameType.setCreateDate(new Date());
		return this.save(gameType);
	}
	
	public GameType update(GameTypeRequest request, GameType existed) throws Exception {
		GameType gameType = new GameType(request);
		Utils.copyNonNullProperties(gameType, existed);
		return save(existed);
	}

	public GameType save(GameType gameType) {
		log.debug("save " + gameType);
		Integer id = gameType.getId();
		log.debug("save " + id);
		return gameTypeRepository.save(gameType);
	}
	public GameType storePhoto(GameType existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = GameType.class.getSimpleName().toLowerCase();
			storageService.store(file, fileName, folder);
			existed.setPhoto(imageUrl + folder + File.separator + fileName);


			if (Utils.isNotEmpty(oldFile) && !Utils.isTestPhoto(oldFile)) {
				storageService.deleteFile(oldFile);
			}
		} catch (Exception ex) {
			log.warn("failed to delete file:{}", ex);
		}
		return save(existed);
	}
	public void delete(Integer id) {
		gameTypeRepository.deleteById(id);
	}
}
