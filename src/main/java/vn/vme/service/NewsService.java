package vn.vme.service;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.guardduty.model.Member;

import vn.vme.common.JConstants.NewsStatus;
import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.News;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.NewsRequest;
import vn.vme.io.game.NewsVO;
import vn.vme.repository.NewsRepository;
import vn.vme.repository.CategoryNewsRepository;

@Service
public class NewsService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(NewsService.class);

	@Autowired
	private NewsRepository newsRepository;
	
	@Autowired
	private CategoryNewsRepository categoryNewsRepository;
	
	@Autowired
	private CategoryNewsService categoryNewsService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	Environment env;

	@Value("${imageUrl}")
	private String imageUrl;
	
	public NewsService() {
		log.debug("NewsService");
	}

	public NewsVO load(NewsVO vo) {
		return vo;
	}
	
	public News findOne(Integer id) {
		return newsRepository.findById(id).orElse(null);
	}

	public Page<News> findByUserId(Long userId, Pageable paging) {
		return newsRepository.findByUserId(userId, paging);
	}

	public Page<News> findAll(Pageable pageable) {
		return newsRepository.findAll(pageable);
	}

	public News save(News news) {
		return newsRepository.save(news);
	}

	public void delete(Integer id) {
		newsRepository.deleteById(id);

	}

	
	public News create(NewsRequest request) {
		//request.setStatus(NewsStatus.ACTIVE.name());
		News news = new News(request);
		Long adminId = UserContextHolder.getContext().getCurrentUser().getId();
		news.setCreateId(adminId);
		news.setCreateDate(new Date());
		news.setCategoryNews(categoryNewsRepository.findById(request.getCategoryId()).get());
		return this.save(news);
	}
	

	public List<News> findByStatus(String status) {
		return newsRepository.findByStatus(status);
	}

	public NewsVO getDetail(Integer id) {
		return getDetail(findOne(id));
	}

	public NewsVO getDetail(News news) {
		NewsVO newsVO = news.getVO();
		newsVO.setCategoryNews(categoryNewsService.load(news.getCategoryNews().getVO()));
		return newsVO;
	}
	public News storePhoto(News existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = News.class.getSimpleName().toLowerCase();
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
	public void changeStatus(News news, NewsStatus status) {
		news.setStatus(status.name());
		this.save(news);
	}

}
