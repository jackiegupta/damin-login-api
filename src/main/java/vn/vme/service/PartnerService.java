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
import vn.vme.entity.Partner;
import vn.vme.io.game.PartnerRequest;
import vn.vme.io.game.PartnerVO;
import vn.vme.repository.PartnerRepository;

@Service
public class PartnerService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(PartnerService.class);

	@Autowired
	private PartnerRepository partnerRepository;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	Environment env;

	@Value("${imageUrl}")
	private String imageUrl;
	
	public PartnerService() {
		log.debug("PartnerService");
	}

	public PartnerVO load(PartnerVO vo) {
		return vo;
	}
	
	public Partner findOne(Integer id) {
		return partnerRepository.findById(id).orElse(null);
	}


	public Page<Partner> findAll(Pageable pageable) {
		return partnerRepository.findAll(pageable);
	}

	public Partner save(Partner partner) {
		return partnerRepository.save(partner);
	}

	public void delete(Integer id) {
		partnerRepository.deleteById(id);

	}

	
	public Partner create(PartnerRequest request) {
		Partner partner = new Partner(request);
		Long adminId = UserContextHolder.getContext().getCurrentUser().getId();
		partner.setCreateId(adminId);
		partner.setCreateDate(new Date());
		return this.save(partner);
	}
	

	public List<Partner> findByStatus(String status) {
		return partnerRepository.findByStatus(status);
	}

	public PartnerVO getDetail(Integer id) {
		return getDetail(findOne(id));
	}

	public PartnerVO getDetail(Partner partner) {
		return partner.getVO();
	}
	public Partner storePhoto(Partner existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = Partner.class.getSimpleName().toLowerCase();
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
