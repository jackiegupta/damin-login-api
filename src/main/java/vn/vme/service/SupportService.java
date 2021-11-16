package vn.vme.service;

import java.io.File;
import java.util.Date;
import java.util.List;
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

import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Support;
import vn.vme.io.game.SupportRequest;
import vn.vme.io.game.SupportVO;
import vn.vme.repository.SupportRepository;

@Service
public class SupportService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(SupportService.class);

	@Autowired
	private SupportRepository supportRepository;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	Environment env;

	@Value("${imageUrl}")
	private String imageUrl;
	
	public SupportService() {
		log.debug("SupportService");
	}

	public SupportVO load(SupportVO vo) {
		return vo;
	}
	
	public Support findOne(Integer id) {
		return supportRepository.findById(id).orElse(null);
	}


	public Page<Support> findAll(Pageable pageable) {
		return supportRepository.findAll(pageable);
	}

	public Support save(Support support) {
		return supportRepository.save(support);
	}

	public void delete(Integer id) {
		supportRepository.deleteById(id);

	}

	
	public Support create(SupportRequest request) {
		Support support = new Support(request);
		Long adminId = UserContextHolder.getContext().getCurrentUser().getId();
		support.setCreateId(adminId);
		support.setCreateDate(new Date());
		return this.save(support);
	}
	

	public List<Support> findByStatus(String status) {
		return supportRepository.findByStatus(status);
	}

	public SupportVO getDetail(Integer id) {
		return getDetail(findOne(id));
	}

	public SupportVO getDetail(Support support) {
		return support.getVO();
	}
	public Support storePhoto(Support existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = Support.class.getSimpleName().toLowerCase();
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
