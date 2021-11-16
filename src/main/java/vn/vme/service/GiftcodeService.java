package vn.vme.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.vme.common.JConstants.CurrencyType;
import vn.vme.common.JConstants.Status;
import vn.vme.common.JConstants.TransactionStatus;
import vn.vme.common.JConstants.TransactionType;
import vn.vme.common.JConstants.TypeChange;
import vn.vme.common.JConstants;
import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Game;
import vn.vme.entity.Giftcode;
import vn.vme.entity.Pool;
import vn.vme.entity.Transaction;
import vn.vme.entity.User;
import vn.vme.entity.UserGiftcode;
import vn.vme.io.game.GiftcodeRequest;
import vn.vme.io.game.GiftcodeVO;
import vn.vme.io.game.UserGiftcodeRequest;
import vn.vme.io.user.UserVO;
import vn.vme.repository.EventRepository;
import vn.vme.repository.GameRepository;
import vn.vme.repository.GiftcodeRepository;
import vn.vme.repository.ItemRepository;
import vn.vme.repository.PoolRepository;
import vn.vme.repository.TransactionRepository;
import vn.vme.repository.UserGiftcodeRepository;
import vn.vme.repository.UserRepository;

@Service
public class GiftcodeService {
	private static final Logger log = LoggerFactory.getLogger(GiftcodeService.class);

	@Autowired
	private GiftcodeRepository giftcodeRepository;
	
	@Autowired
	private UserGiftcodeRepository userGiftcodeRepository;
	
	@Autowired
	public TransactionService transactionService;
	
	@Autowired
	private PoolRepository poolRepository;
	
	@Autowired
	private GameRepository gameRepository;

	//@Autowired(required=false)
    //MessagePublisher messagePublisher;

	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RestTemplate restTemplate;
	public GiftcodeService() {
	}
	
	public GiftcodeVO load(GiftcodeVO vo) {
		return vo;
	}
	public Giftcode findOne(Long id) {
		return giftcodeRepository.findById(id).get();
	}
	public Page<Giftcode> search(GiftcodeRequest request, Pageable pageable) {
		
		return giftcodeRepository.search(request, pageable);
	}
	
	public Iterable<Giftcode> findAll() {
		return giftcodeRepository.findAll();
	}
	public Giftcode create(GiftcodeRequest request) {
		request.setStatus(Status.INACTIVE.name());
		Giftcode giftcode = new Giftcode(request);
		if(giftcode.getCode() != null) {
			giftcode.setCode(giftcode.getCode().toUpperCase());
		}
		if(request.getPoolId()!=null && request.getPoolId()>0) {
			Pool findById = poolRepository.findById(request.getPoolId()).get();
			giftcode.setPool(findById);
		}
		if(request.getGameId()!=null && request.getGameId()>0) {
			Game findById = gameRepository.findById(request.getGameId()).get();
			giftcode.setGame(findById);
		}
		giftcode.setCreateDate(new Date());
		this.save(giftcode);
		return giftcode;
	}
	
	public Giftcode update(GiftcodeRequest request, Giftcode existed) throws Exception {
		log.debug("update " + request);
		Giftcode giftcode = new Giftcode(request);
		Utils.copyNonNullProperties(giftcode, existed);
		if (request.getUserId() != null && request.getUserId() > 0) {
			existed.setUser(userService.findOne(request.getUserId()));
		}
		existed = save(existed);
		return existed;
	}

	public Giftcode save(Giftcode giftcode) {
		giftcode = giftcodeRepository.save(giftcode);
		log.debug("save " + giftcode);
		return giftcode;
	}
	
	public void delete(Long id) {
		giftcodeRepository.deleteById(id);
	}

	public Giftcode findByCode(String code) {
		return giftcodeRepository.findByCode(code);
	}
	public Giftcode updateRandomGiftcodeGame(UserGiftcodeRequest request, Giftcode existed) throws Exception {
		UserVO userVO = UserContextHolder.getContext().getCurrentUser();
		
		User user = userService.findOne(userVO.getId());
		UserGiftcode userGiftcode = new UserGiftcode();
		BeanUtils.copyProperties(request, userGiftcode);
		userGiftcode.setGiftcode(existed);
		userGiftcode.setUser(user);
		userGiftcode.setStatus(Status.ACTIVE.name());
		userGiftcode.setCreateDate(new Date());
		userGiftcode.setUpdateDate(new Date());
		userGiftcodeRepository.save(userGiftcode);
		log.info("updateGiftcodeGame " + userGiftcode);
		//cộng các item của giftcode cho user

		transactionService.savePool(existed.getPool(), user);
		
		existed.setStatus(Status.INACTIVE.name());
		existed = save(existed);
		log.info("updateGiftcode " + existed);
		return existed;
	}
	
	public UserGiftcode useGiftcodeGame(UserGiftcodeRequest request, UserGiftcode userGiftcode) throws Exception {
		userGiftcode.setServerId(request.getServerId());
		userGiftcode.setStatus(Status.INACTIVE.name());
		userGiftcode.setActorName(request.getActorName());
		userGiftcode.setCreateDate(new Date());
		userGiftcode.setUpdateDate(new Date());
		userGiftcode.setGame(gameRepository.findById(request.getGameId()).get());
		userGiftcodeRepository.save(userGiftcode);
		log.info("updateGiftcodeGame " + userGiftcode);
		return userGiftcode;
	}
}
