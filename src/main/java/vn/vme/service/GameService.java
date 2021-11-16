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

import vn.vme.common.JConstants;
import vn.vme.common.JConstants.Status;
import vn.vme.common.JConstants.TransactionStatus;
import vn.vme.common.JConstants.TransactionType;
import vn.vme.common.JConstants.TypeChange;
import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Game;
import vn.vme.entity.Transaction;
import vn.vme.entity.User;
import vn.vme.entity.UserGame;
import vn.vme.exception.NotEnoughBalanceException;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.GameRequest;
import vn.vme.io.game.GameVO;
import vn.vme.io.game.UserGameRequest;
import vn.vme.io.rest360.Game360;
import vn.vme.io.rest360.User360;
import vn.vme.io.user.UserVO;
import vn.vme.repository.GameRepository;
import vn.vme.repository.GameTypeRepository;
import vn.vme.repository.PublishRepository;
import vn.vme.repository.TransactionRepository;
import vn.vme.repository.UserGameRepository;
import vn.vme.repository.UserRepository;

@Service
public class GameService {
	private static final Logger log = LoggerFactory.getLogger(GameService.class);

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private Rest360Service rest360Service;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	
	@Autowired
	private UserGameRepository userGameRepository;
	
	@Autowired
	private GameTypeRepository gameTypeRepository;

	@Autowired
	private PublishRepository publishRepository;


	@Value("${imageUrl}")
	private String imageUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	RestTemplate restTemplate;
	
	public GameService() {
	}
	public GameVO load(GameVO vo) {
		return vo;
	}
	public Game findOne(Integer id) {
		return gameRepository.findById(id).get();
	}
	public Page<Game> search(GameRequest request, Pageable pageable) {
		if(Utils.isNotEmpty(request.getName())) {
			request.setName(request.getName().toLowerCase());
		}
		return gameRepository.search(request, pageable);
	}
	
	public Iterable<Game> findAll() {
		return gameRepository.findAll();
	}
	public Game register(GameRequest request) {
		UserVO user = UserContextHolder.getContext().getCurrentUser();
		Game game = this.findOne(request.getId());
		if (game == null) {
			throw new NotFoundException("Game Id " + request.getId() + " không tồn tại");
		}
		UserGame existed = userGameRepository.findByUserIdAndGameId(user.getId(), game.getId());
		if (existed == null) {
			getPayment(user,game);
		}else {
			existed.setUpdateDate(new Date());
		}
		return this.save(game);
	}
	
	public User360 login(GameRequest request) {
		UserVO user = UserContextHolder.getContext().getCurrentUser();
		Game game = this.findOne(request.getId());
		if (game == null) {
			throw new NotFoundException("Game Id " + request.getId() + " không tồn tại");
		}
		UserGame existed = userGameRepository.findByUserIdAndGameId(user.getId(), game.getId());
		if (existed == null) {
			throw new NotFoundException("User chưa đăng kí game " + request.getId());
		}else {
			return loginToGame(user);
		}
	}
	
	public Game transfer(UserGameRequest request) {
		UserVO user = UserContextHolder.getContext().getCurrentUser();
		Game game = this.findOne(request.getGameId());
		if (game == null) {
			throw new NotFoundException("Game Id " + request.getId() + " không tồn tại");
		}
		
		try {
			UserGame userGame = userGameRepository.findByUserIdAndGameId(user.getId(), game.getId());
			Game360 game360 = rest360Service.transfer( userGame , request.getAmount());
			log.debug("Game360 " + game360 );
			//userGame.setId360(user360.getId());
		}catch (Exception e) {
			e.printStackTrace();
			log.debug("rest360Service " + e.getMessage() );
		}
		
		return this.save(game);
	}
	
	public User360 profile() {
		UserVO userVO = UserContextHolder.getContext().getCurrentUser();
		User user = userRepository.findById(userVO.getId()).get();
		try {
			User360 user360 = rest360Service.updateProfile(user);
			log.debug("User360 " + user360.getId() );
			return user360;
		}catch (Exception e) {
			e.printStackTrace();
			log.debug("rest360Service " + e.getMessage() );
			return null;
		}
		
	}
	
	private User360 loginToGame(UserVO userVO) {
		User user = userRepository.findById(userVO.getId()).get();
		try {
			return rest360Service.login(user);
		}catch (Exception e) {
			e.printStackTrace();
			log.debug("rest360Service " + e.getMessage() );
			return null;
		}
		
	}
	
	private void getPayment(UserVO userVO, Game game) {
		String titleTrans = "Đăng ký game "+ game.getName();
		String detailTrans = titleTrans + "###Before: " + userVO.toString();
		
		User user = userRepository.findById(userVO.getId()).get();
		Integer rice = user.getRice();
		Integer amount = game.getAmount();
		if (rice < amount) {
			throw new NotEnoughBalanceException("Tài khoản không đủ đăng ký game");
		}else {
			// Update rice user
			user.setRice(rice-amount);
			user.setUpdateDate(new Date());
			userRepository.save(user);
			//pay transaction store
			detailTrans =  detailTrans + "###After: " + user.toString();
			detailTrans =  detailTrans + "###Game: " + game.toString();
			Transaction payment = new Transaction(amount,titleTrans);
			payment.setFromUser(new User(user.getId()));
			payment.setToUser(new User(JConstants.ADMIN_ID));
			payment.setStatus(TransactionStatus.APPROVED.name());
			payment.setType(TransactionType.BUYGAME.name());
			payment.setTypeChange(TypeChange.MINUS.name());
			payment.setContent(detailTrans);
			payment.setCurrency(game.getCurrency());
			payment.setCreateDate(new Date());
			payment.setUpdateDate(new Date());
			transactionRepository.save(payment);
			UserGame userGame = new UserGame();
			
			userGame.setStatus(Status.ACTIVE.name());
			userGame.setAmount(game.getAmount());
			userGame.setGame(game);
			userGame.setUser(user);
			
			userGame.setCreateDate(new Date());
			userGame.setUpdateDate(new Date());
			userGameRepository.save(userGame);
		}
		
	}

	public Game create(GameRequest request) {
		request.setStatus(Status.ACTIVE.name());
		Game game = new Game(request);
		game.setGameType(gameTypeRepository.findById(request.getGameTypeId()).get());
		game.setPublish(publishRepository.findById(request.getPublishId()).get());
		game.setCreateDate(new Date());
		return this.save(game);
	}
	
	public Game update(GameRequest request, Game existed) throws Exception {
		Game game = new Game(request);
		if(request.getGameTypeId() != null)
			game.setGameType(gameTypeRepository.findById(request.getGameTypeId()).get());
		if(request.getPublishId() != null)
			game.setPublish(publishRepository.findById(request.getPublishId()).get());
		game.setUpdateDate(new Date());
		Utils.copyNonNullProperties(game, existed);
		return save(existed);
	}

	public Game save(Game game) {
		log.debug("save " + game);
		Integer id = game.getId();
		game = gameRepository.save(game);
		log.debug("save " + id);
		return game;
	}
	public Game storePhoto(Game existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = Game.class.getSimpleName().toLowerCase();
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
		gameRepository.deleteById(id);
	}
}
