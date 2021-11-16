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
import vn.vme.entity.CategoryNews;
import vn.vme.io.game.CategoryNewsRequest;
import vn.vme.io.game.CategoryNewsVO;
import vn.vme.repository.CategoryNewsRepository;

@Service
public class CategoryNewsService {
	private static final Logger log = LoggerFactory.getLogger(CategoryNewsService.class);

	@Autowired
	private CategoryNewsRepository categoryNewsRepository;
	

	@Value("${imageUrl}")
	private String imageUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	RestTemplate restTemplate;
	
	public CategoryNewsService() {
	}
	public CategoryNewsVO load(CategoryNewsVO vo) {
		return vo;
	}
	public CategoryNews findOne(Integer id) {
		return categoryNewsRepository.findById(id).get();
	}
	public Page<CategoryNews> search(CategoryNewsRequest request, Pageable pageable) {
		if(Utils.isNotEmpty(request.getName())) {
			request.setName(request.getName().toLowerCase());
		}
		return categoryNewsRepository.search(request, pageable);
	}
	
	public Iterable<CategoryNews> findAll() {
		return categoryNewsRepository.findAll();
	}
	
	public CategoryNews create(CategoryNewsRequest request) {
		//request.setStatus(Status.ACTIVE.name());
		CategoryNews categoryNews = new CategoryNews(request);
		categoryNews.setCreateDate(new Date());
		return this.save(categoryNews);
	}
	
	public CategoryNews update(CategoryNewsRequest request, CategoryNews existed) throws Exception {
		CategoryNews categoryNews = new CategoryNews(request);
		Utils.copyNonNullProperties(categoryNews, existed);
		return save(existed);
	}

	public CategoryNews save(CategoryNews categoryNews) {
		log.debug("save " + categoryNews);
		Integer id = categoryNews.getId();
		log.debug("save " + id);
		return categoryNewsRepository.save(categoryNews);
	}
	public CategoryNews storePhoto(CategoryNews existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = CategoryNews.class.getSimpleName().toLowerCase();
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
		categoryNewsRepository.deleteById(id);
	}
}
