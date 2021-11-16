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
import vn.vme.entity.Publish;
import vn.vme.io.game.PublishRequest;
import vn.vme.io.game.PublishVO;
import vn.vme.repository.PublishRepository;

@Service
public class PublishService {
	private static final Logger log = LoggerFactory.getLogger(PublishService.class);

	@Autowired
	private PublishRepository publishRepository;

	//@Autowired(required=false)
    //MessagePublisher messagePublisher;

	@Value("${imageUrl}")
	private String imageUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	RestTemplate restTemplate;
	public PublishService() {
	}
	
	public PublishVO load(PublishVO vo) {
		return vo;
	}
	public Publish findOne(Integer id) {
		return publishRepository.findById(id).get();
	}
	public Page<Publish> search(PublishRequest request, Pageable pageable) {
		
		return publishRepository.search(request, pageable);
	}
	
	public Iterable<Publish> findAll() {
		return publishRepository.findAll();
	}
	public Publish create(PublishRequest request) {
		//request.setStatus(Status.ACTIVE.name());
		Publish publish = new Publish(request);
		publish.setCreateDate(new Date());
		return this.save(publish);
	}
	
	public Publish update(PublishRequest request, Publish existed) throws Exception {
		Publish publish = new Publish(request);
		Utils.copyNonNullProperties(publish, existed);
		return save(existed);
	}

	public Publish save(Publish publish) {
		log.debug("save " + publish);
		Integer id = publish.getId();
		publish = publishRepository.save(publish);
		log.debug("save " + id);
		return publish;
	}
	public Publish storePhoto(Publish existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = Publish.class.getSimpleName().toLowerCase();
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
		publishRepository.deleteById(id);
	}
}
