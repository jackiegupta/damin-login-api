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
import vn.vme.entity.Banner;
import vn.vme.io.game.BannerRequest;
import vn.vme.io.game.BannerVO;
import vn.vme.repository.BannerRepository;

@Service
public class BannerService {
	private static final Logger log = LoggerFactory.getLogger(BannerService.class);

	@Autowired
	private BannerRepository bannerRepository;
	
	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Value("${imageUrl}")
	private String imageUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	RestTemplate restTemplate;
	public BannerService() {
	}
	
	public BannerVO load(BannerVO vo) {
		return vo;
	}
	public Banner findOne(Integer id) {
		return bannerRepository.findById(id).get();
	}
	public Page<Banner> search(BannerRequest request, Pageable pageable) {
		
		return bannerRepository.search(request, pageable);
	}
	
	public Iterable<Banner> findAll() {
		return bannerRepository.findAll();
	}
	public Banner create(BannerRequest request) {
		request.setStatus(Status.INACTIVE.name());
		Banner banner = new Banner(request);
		if(request.getGameId() != null &&  request.getGameId() > 0) {
			banner.setGame(gameService.findOne(request.getGameId()));
		}
		banner.setCreateDate(new Date());
		return this.save(banner);
	}
	
	public Banner update(BannerRequest request, Banner existed) throws Exception {
		log.debug("update " + request);
		Banner banner = new Banner(request);
		if(request.getGameId() != null && request.getGameId() > 0) {
			banner.setGame(gameService.findOne(request.getGameId()));
		}
		Utils.copyNonNullProperties(banner, existed);
		return save(existed);
	}

	public Banner save(Banner banner) {
		log.debug("save " + banner);
		Integer id = banner.getId();
		log.debug("save " + id);
		return bannerRepository.save(banner);
	}
	
	public void delete(Integer id) {
		bannerRepository.deleteById(id);
	}
	
	public Banner storePhoto(Banner existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = Banner.class.getSimpleName().toLowerCase();
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

}
