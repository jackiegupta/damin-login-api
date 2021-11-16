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
import vn.vme.entity.Setting;
import vn.vme.io.game.SettingRequest;
import vn.vme.io.game.SettingVO;
import vn.vme.repository.SettingRepository;

@Service
public class SettingService {
	private static final Logger log = LoggerFactory.getLogger(SettingService.class);

	@Autowired
	private SettingRepository settingRepository;
	
	//@Autowired(required=false)
    //MessagePublisher messagePublisher;

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
	
	
	public SettingService() {
	}
	
	public SettingVO load(SettingVO vo) {
		return vo;
	}
	public Setting findOne(Integer id) {
		return settingRepository.findById(id).get();
	}
	public Page<Setting> search(SettingRequest request, Pageable pageable) {
		
		return settingRepository.search(request, pageable);
	}
	
	public Iterable<Setting> findAll() {
		return settingRepository.findAll();
	}
	public Setting create(SettingRequest request) {
		request.setStatus(Status.INACTIVE.name());
		Setting setting = new Setting(request);
		setting.setCreateDate(new Date());
		this.save(setting);
		return setting;
	}
	
	public Setting update(SettingRequest request, Setting existed) throws Exception {
		log.debug("update " + request);
		Setting setting = new Setting(request);
		Utils.copyNonNullProperties(setting, existed);
		existed = save(existed);
		return existed;
	}

	public Setting save(Setting setting) {
		log.debug("save " + setting);
		Integer id = setting.getId();
		setting = settingRepository.save(setting);
		log.debug("save " + id);
		return setting;
	}
	
	public void delete(Integer id) {
		settingRepository.deleteById(id);
	}

}
