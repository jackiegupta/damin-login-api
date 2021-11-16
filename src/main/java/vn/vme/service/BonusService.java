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

import vn.vme.common.JConstants.CurrencyType;
import vn.vme.common.JConstants.Status;
import vn.vme.common.JConstants.TransactionStatus;
import vn.vme.common.JConstants.TransactionType;
import vn.vme.common.JConstants.TypeChange;
import vn.vme.common.JConstants;
import vn.vme.common.Utils;
import vn.vme.entity.Bonus;
import vn.vme.entity.Transaction;
import vn.vme.entity.User;
import vn.vme.entity.UserBonus;
import vn.vme.io.game.BonusRequest;
import vn.vme.io.game.BonusVO;
import vn.vme.repository.BonusRepository;
import vn.vme.repository.TransactionRepository;
import vn.vme.repository.UserBonusRepository;
import vn.vme.repository.UserRepository;

@Service
public class BonusService {
	private static final Logger log = LoggerFactory.getLogger(BonusService.class);

	@Autowired
	private BonusRepository bonusRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;

	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	public UserBonusRepository userBonusRepository;
	
	@Autowired
	StorageService storageService;
	
	@Value("${imageUrl}")
	private String imageUrl;
	
	
	@Autowired
	RestTemplate restTemplate;
	public BonusService() {
	}
	
	public BonusVO load(BonusVO vo) {
		return vo;
	}
	public Bonus findOne(Integer id) {
		return bonusRepository.findById(id).get();
	}
	public Page<Bonus> search(BonusRequest request, Pageable pageable) {
		
		return bonusRepository.search(request, pageable);
	}
	
	public Iterable<Bonus> findAll() {
		return bonusRepository.findAll();
	}
	
	
	public Bonus create(BonusRequest request) {
		request.setStatus(Status.ACTIVE.name());
		Bonus bonus = new Bonus(request);
		bonus.setCreateDate(new Date());
		this.save(bonus);
		return bonus;
	}
	
	public Bonus update(BonusRequest request, Bonus existed) throws Exception {
		log.debug("update " + request);
		Bonus bonus = new Bonus(request);
		Utils.copyNonNullProperties(bonus, existed);
		existed = save(existed);
		return existed;
	}

	public Bonus save(Bonus bonus) {
		log.debug("save " + bonus);
		Integer id = bonus.getId();
		bonus = bonusRepository.save(bonus);
		log.debug("save " + id);
		return bonus;
	}
	
	public void delete(Integer id) {
		bonusRepository.deleteById(id);
	}
	
	public Bonus storePhoto(Bonus existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = Bonus.class.getSimpleName().toLowerCase();
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

	public void saveUserBonus(UserBonus userBonus) {
		
		userBonusRepository.save(userBonus);
		User user = userBonus.getUser();
		
		Integer seed = userBonus.getBonus().getSeed();
		//pay transaction store
		String bonusName = userBonus.getBonus().getName();
		if(seed > 0) {
			String titleTransSeed = bonusName + " nhận thóc";
			String detailTransSeed = titleTransSeed + "###Before: " + user.toString();
			user.setSeed(user.getSeed() + seed);
			userRepository.save(user);
			//pay transaction store
			detailTransSeed =  detailTransSeed + "###After: " + user.toString();
			Transaction paymentSeed = new Transaction(seed, titleTransSeed);
			paymentSeed.setFromUser(new User(user.getId()));
			paymentSeed.setToUser(new User(JConstants.ADMIN_ID));
			paymentSeed.setStatus(TransactionStatus.APPROVED.name());
			paymentSeed.setContent(detailTransSeed);
			paymentSeed.setType(TransactionType.BONUS.name());
			paymentSeed.setTypeChange(TypeChange.PLUS.name());
			paymentSeed.setCurrency(CurrencyType.SEED.name());
			paymentSeed.setCreateDate(new Date());
			paymentSeed.setUpdateDate(new Date());
			transactionRepository.save(paymentSeed);
		}
		
		//if bonus.rice > 0
		Integer rice = userBonus.getBonus().getRice();
		if(rice > 0) {
			String titleTransRice = bonusName + " nhận gạo";
			String detailTransRice = titleTransRice + "###Before: " + user.toString();
			user.setRice(user.getRice() + rice);
			userRepository.save(user);
			//pay transaction store
			detailTransRice =  detailTransRice + "###After: " + user.toString();
			Transaction paymentRice = new Transaction(seed, titleTransRice);
			paymentRice.setFromUser(new User(user.getId()));
			paymentRice.setToUser(new User(JConstants.ADMIN_ID));
			paymentRice.setStatus(TransactionStatus.APPROVED.name());
			paymentRice.setContent(detailTransRice);
			paymentRice.setType(TransactionType.BONUS.name());
			paymentRice.setTypeChange(TypeChange.PLUS.name());
			paymentRice.setCurrency(CurrencyType.RICE.name());
			paymentRice.setCreateDate(new Date());
			paymentRice.setUpdateDate(new Date());
			transactionRepository.save(paymentRice);
		}
		
		//if bonus.exp > 0
		Integer exp = userBonus.getBonus().getExpPoint();
		if(exp > 0) {
			String titleTransExp = bonusName + " nhận nhận Exp";
			String detailTransExp = titleTransExp + "###Before: " + user.toString();
			user.setExp(user.getExp() + exp);
			userRepository.save(user);
			//pay transaction store
			detailTransExp =  detailTransExp + "###After: " + user.toString();
			Transaction paymentExp = new Transaction(seed, titleTransExp);
			paymentExp.setFromUser(new User(user.getId()));
			paymentExp.setToUser(new User(JConstants.ADMIN_ID));
			paymentExp.setStatus(TransactionStatus.APPROVED.name());
			paymentExp.setContent(detailTransExp);
			paymentExp.setType(TransactionType.BONUS.name());
			paymentExp.setTypeChange(TypeChange.PLUS.name());
			paymentExp.setCurrency(CurrencyType.POINT.name());
			paymentExp.setCreateDate(new Date());
			paymentExp.setUpdateDate(new Date());
			transactionRepository.save(paymentExp);
		}
		
	}
}
