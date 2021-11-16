package vn.vme.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.vme.common.DateUtils;
import vn.vme.common.JConstants;
import vn.vme.common.JConstants.CurrencyType;
import vn.vme.common.JConstants.Roles;
import vn.vme.common.JConstants.Status;
import vn.vme.common.JConstants.TransactionStatus;
import vn.vme.common.JConstants.TransactionType;
import vn.vme.common.JConstants.TypeChange;
import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Product;
import vn.vme.entity.Task;
import vn.vme.entity.Transaction;
import vn.vme.entity.User;
import vn.vme.entity.UserTask;
import vn.vme.event.UserChangeHandler;
import vn.vme.exception.EmailException;
import vn.vme.exception.ExistedDataException;
import vn.vme.exception.ExistedEmailException;
import vn.vme.exception.ExistedNameException;
import vn.vme.exception.ExistedPhoneException;
import vn.vme.exception.InvalidKeyException;
import vn.vme.exception.KeyExpriredException;
import vn.vme.exception.UserLockedException;
import vn.vme.exception.UserNotFoundException;
import vn.vme.io.game.UserTaskRequest;
import vn.vme.io.rest360.User360;
import vn.vme.io.user.PasswordRequest;
import vn.vme.io.user.UserRequest;
import vn.vme.io.user.UserVO;
import vn.vme.io.user.VerifyRequest;
import vn.vme.repository.AttendRepository;
import vn.vme.repository.SettingRepository;
import vn.vme.repository.TransactionRepository;
import vn.vme.repository.UserRepository;
import vn.vme.repository.UserTaskRepository;

@Service
public class UserService{
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	SettingRepository settingRepository;
	
	@Autowired
	AttendRepository attendRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired(required = false)
	public TaskService taskService;
	@Autowired(required = false)
	public TransactionService transactionService;
	
	@Autowired
    public UserTaskRepository userTaskRepository;
	
	@Autowired
	public TransactionRepository userTransactionRepository;
	
	@Value("${testPhone}")
	private String testPhone;
	
	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Value("${referUrl}")
	private String referUrl;

	@Autowired
	protected Environment env;

	@Autowired
	UserChangeHandler userChangeHandler;
	
	@Value("${imageUrl}")
	private String imageUrl;
	
	@Autowired
	private Rest360Service rest360Service;
	
	public UserService() {
	}
	
	public UserVO load(UserVO user) {
		String level = user.getLevel();
		String vip = user.getVip();
		if(user.isTopLevel()) {
			String exp = settingRepository.findByCode(level).getValue();
			user.setExpCount(Integer.parseInt(exp));
		}else {
			String exp = settingRepository.findByCode(level).getValue();
			user.setExpCount(Integer.parseInt(exp) - user.getExp());
			user.setNextLevel(Utils.getNextLevel(level));
		}
		if(Utils.isNotEmpty(vip)) {
			String point = settingRepository.findByCode(vip).getValue();
			if(user.isTopVip()) {
				user.setPointCount(Integer.parseInt(point));
			}else {
				user.setNextVip(Utils.getNextVip(vip));
				user.setPointCount(Integer.parseInt(point) - user.getPoint());
			}
		}
		//user.setReferLink(webUrl+ "/#/refer?referCode="+user.getReferCode());
		user.setReferLink(referUrl +user.getReferCode());
		return user;
	}
	
	
	
	public User findOne(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public List<GrantedAuthority> retrieveAuthorities(Long userId){
		List<GrantedAuthority> authorities = new ArrayList<>(); 
		
		List<String> roles = roleService.getRoleByUserId(userId);

		roles.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role));
		});
		return authorities;
	}
	
	public User findByUserNameOrEmailOrPhone(String userName, String email,String phone) {
		User user = userRepository.findByUserNameOrEmailOrPhone(userName, email,phone);
		return user;
	}
	
	public User findByUserName(String userName) {
		if(Utils.isEmpty(userName)) {
			return null;
		}
		return userRepository.findByUserName(userName);
	}
	
	public User createUser(UserRequest request) throws Exception {
		String userName = request.getUserName();
		if(userName.contains("@")) {
			request.setEmail(userName);
		}
//		else {
//			request.setPhone(userName);
//		}
		String email = request.getEmail();
		String phone = request.getPhone();
		User userByUserName = this.findByUserName(userName);
		User userByEmail = this.findByEmail(email);
		User userByPhone = this.findByPhone(phone);
		User userByAnd = this.findByUserNameAndEmailAndPhone(userName, email, phone);
		if (userByUserName == null && userByEmail==null && userByPhone ==null) {
			User user = new User(request);
			user.setDefault();
			boolean isUser = request.getRoles() == null;
			if (isUser) {
				user.addRole(Roles.USER);
			}
			
			String folder = User.class.getSimpleName().toLowerCase();
			user.setPhoto(imageUrl + folder + File.separator + "avatar.png");
			user.setReferCode(Utils.genReferCode(6));
			this.save(user);
			log.debug("Registered fromUser Id [" + user.getId() + "]");
			
			return user;
		} else if (userByUserName != null) {
			boolean unverify = userByAnd.getStatus().equals(Status.INACTIVE.name());
			if (unverify) {
				log.debug(userName + "đã đăng ký nhưng [" + userName + "] chưa kích hoạt.");
				this.save(userByAnd);
				return userByAnd;
			}else {
				throw new ExistedDataException("User đã tồn tại " + request);
			}
		} else 	if (userByEmail != null) {
				throw new ExistedEmailException("Email [" + email + "] đã tồn tại");
		} else if (userByPhone != null) {
				throw new ExistedNameException("Tài khoản [" + userName + "] đã tồn tại");
		} else {
				throw new RuntimeException("Không thể đăng ký user " + request);
		}
	}
	public User createUserNotOTP(UserRequest request) throws Exception {
		String userName = request.getUserName();
		if(userName.contains("@")) {
			request.setEmail(userName);
		}
//		else {
//			request.setPhone(userName);
//		}
		String email = request.getEmail();
		String phone = request.getPhone();
		User userByUserName = this.findByUserName(userName);
		User userByEmail = this.findByEmail(email);
		User userByPhone = this.findByPhone(phone);
		User userByAnd = this.findByUserNameAndEmailAndPhone(userName, email, phone);
		if (userByUserName == null && userByEmail==null && userByPhone ==null) {
			User user = new User(request);
			user.setDefault();
			/*
			 * boolean isUser = request.getRoles() == null; if (isUser) {
			 * user.addRole(Roles.USER); }
			 */
			
			String folder = User.class.getSimpleName().toLowerCase();
			user.setPhoto(imageUrl + folder + File.separator + "avatar.png");
			user.setReferCode(Utils.genReferCode(6));
			user.setStatus(Status.ACTIVE.name());
			String encodePassword = new BCryptPasswordEncoder().encode(request.getPassword());
			user.setPassword(encodePassword);
			user.setLoginCount(0);
			this.saveNotOTP(user);
			log.debug("Registered fromUser Id [" + user.getId() + "]");
			
			/*
			try {
				User360 user360 = rest360Service.register(user);
				if(user360 != null) {
					log.debug("User360 " + user360.getUserId() );
					user.setUserId360(user360.getUserId());
					this.saveNotOTP(user);
				}else {
					throw new RuntimeException("Kết nối user " + request);
				}
			}catch (Exception e) {
				e.printStackTrace();
				log.debug("rest360Service " + e.getMessage() );
				throw new RuntimeException("Kết nối user " + request);
			}*/

			return user;
			
			
		} else if (userByUserName != null) {
			boolean unverify = userByAnd.getStatus().equals(Status.INACTIVE.name());
			if (unverify) {
				log.debug(userName + "đã đăng ký nhưng [" + userName + "] chưa kích hoạt.");
				this.saveNotOTP(userByAnd);
				return userByAnd;
			}else {
				throw new ExistedDataException("User đã tồn tại " + request);
			}
		} else 	if (userByEmail != null) {
			throw new ExistedEmailException("Email [" + email + "] đã tồn tại");
		} else if (userByPhone != null) {
			throw new ExistedNameException("Tài khoản [" + userName + "] đã tồn tại");
		} else {
			throw new RuntimeException("Không thể đăng ký user " + request);
		}
	}
	
	public User findByPhone(String phone) {
		if(Utils.isEmpty(phone)) {
			return null;
		}
		return userRepository.findByPhone(phone);
	}

	public User save(User user) throws Exception {
		Long id = user.getId();
		String status = user.getStatus();
		String userName = user.getUserName();
		boolean isNew = id == null || id==0 || status.equals(Status.INACTIVE.name());
		if(isNew){
			String verifyCode = String.valueOf(Utils.random(6));
			log.debug("Generate Random verify code");
			user.setVerifyCode(verifyCode);
			user.setCreateDate(new Date());
			user.setUpdateDate(new Date());
			user.setLastLogin(new Date());
			user.setStatus(Status.INACTIVE.name());
			
			log.info("Publish UserChange fromUser "+ user);
			user = userRepository.save(user);
			VerifyRequest verifyRequest = new VerifyRequest(userName,verifyCode);
			userChangeHandler.sendVerifyRegister(user.getVO(),verifyRequest);
		}else {
			log.debug("Updated "+ user);
			user.setUpdateDate(new Date());
			user.setLastLogin(new Date());
			user = userRepository.save(user);
		}
		return user;
	}
	public User saveNotOTP(User user) throws Exception {
		Long id = user.getId();
		boolean isNew = id == null || id==0;
		if(isNew){
			String verifyCode = String.valueOf(Utils.random(6));
			log.debug("Generate Random verify code");
			user.setVerifyCode(verifyCode);
			user.setCreateDate(new Date());
			user.setUpdateDate(new Date());
			user.setLastLogin(new Date());
			
			log.info("Publish UserChange fromUser "+ user);
			user = userRepository.save(user);
		}else {
			log.debug("Updated "+ user);
			user.setUpdateDate(new Date());
			user.setLastLogin(new Date());
			user = userRepository.save(user);
		}
		return user;
	}
	public void referFriend(User user, User friend) throws Exception {
		String titleTrans = "Mời bạn bè nhận thóc";
		String detailTrans1 = titleTrans + "###Before: " + user.toString();
		String detailTrans2 = titleTrans + "###Before: " + friend.toString();
		String seed = settingRepository.findByCode("INVITE").getValue();
		int seedReceive = Integer.parseInt(seed);
		user.setSeed(user.getSeed() + seedReceive);
		user.setFriendCode(friend.getReferCode());
		this.save(user);
		//pay transaction store
		detailTrans1 =  detailTrans1 + "###After: " + user.toString();
		detailTrans1 =  detailTrans1 + "###ReferCode: " + friend.getReferCode();
		Transaction payment1 = new Transaction(seedReceive, titleTrans);
		payment1.setFromUser(new User(user.getId()));
		payment1.setToUser(new User(friend.getId()));
		payment1.setStatus(TransactionStatus.APPROVED.name());
		payment1.setContent(detailTrans1);
		payment1.setType(TransactionType.EXCHANGERICE.name());
		payment1.setTypeChange(TypeChange.PLUS.name());
		payment1.setCurrency(CurrencyType.SEED.name());
		payment1.setCreateDate(new Date());
		payment1.setUpdateDate(new Date());
		transactionRepository.save(payment1);
		
		// + 100 Friend
		friend.setSeed(user.getSeed() + seedReceive);
		this.save(friend);
		
		//pay transaction store
		detailTrans2 =  detailTrans2 + "###After: " + user.toString();
		detailTrans2 =  detailTrans2 + "###ReferCode: " + friend.getReferCode();
		Transaction payment2 = new Transaction(seedReceive, titleTrans);
		payment2.setFromUser(new User(friend.getId()));
		payment2.setToUser(new User(user.getId()));
		payment2.setStatus(TransactionStatus.APPROVED.name());
		payment2.setContent(detailTrans2);
		payment2.setType(TransactionType.EXCHANGERICE.name());
		payment2.setTypeChange(TypeChange.PLUS.name());
		payment2.setCurrency(CurrencyType.SEED.name());
		payment2.setCreateDate(new Date());
		payment2.setUpdateDate(new Date());
		transactionRepository.save(payment2);
	}
	
	//doi gao lay thoc
	public User exchangeRice(Product product, User user) throws Exception {
		String titleTrans = "Đổi gạo lấy thóc";
		String detailTrans = titleTrans + "###Before: " + user.toString();
		Integer seedAdd = product.getPrice();
		if(product.getDiscount() > 0 ) {
			seedAdd = seedAdd + seedAdd*product.getDiscount()/100;
		}
		user.setSeed(user.getSeed() + seedAdd);
		user.setRice(user.getRice() - product.getAmount());
		this.save(user);
		
		//pay transaction store
		detailTrans =  detailTrans + "###After: " + user.toString();
		detailTrans =  detailTrans + "###Product: " + product.toString();
		Transaction payment = new Transaction(product.getAmount(), titleTrans);
		payment.setFromUser(new User(user.getId()));
		payment.setToUser(new User(JConstants.ADMIN_ID));
		payment.setStatus(TransactionStatus.APPROVED.name());
		payment.setContent(detailTrans);
		payment.setType(TransactionType.EXCHANGERICE.name());
		payment.setTypeChange(TypeChange.PLUS.name());
		payment.setCurrency(CurrencyType.SEED.name());
		payment.setCreateDate(new Date());
		payment.setUpdateDate(new Date());
		transactionRepository.save(payment);
		
		try {
            //TASK RECHARGE
            Task task = taskService.findByCode("TRANSFER_RICE2SEED");
            if(task != null) {
            	UserTaskRequest userTaskRequest = new UserTaskRequest("", user.getId(), 0, 0 ,task.getId(), "", "", "");
            	Date date = new Date();
            	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");//05-02-2020
            	String datetime = formatter.format(date);
            	userTaskRequest.setUpdateDate(datetime + " 00:00:00");
            	List<UserTask> list = userTaskRepository.findUserTask(userTaskRequest);
            	if(list !=null && list.size() >= 0) {
            		log.info("có TASK TRANSFER_RICE2SEED");
            	}else {
            		log.info("Chưa có TASK TRANSFER_RICE2SEED");
            		UserTask userTask = new UserTask();
            		userTask.setUser(user);
            		userTask.setTask(task);
            		userTask.setCreateDate(new Date());
            		userTask.setUpdateDate(new Date());
            		userTaskRepository.save(userTask);
        			transactionService.saveTask(task, user);
            	}
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
		
		return user;
	}
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
	
	public void delete(Long id) {
		userRepository.deleteById(id);
	}
	public void deleteByUserName(String userName) {
		User user = userRepository.findByEmail(userName);
		if (user != null) {
			userRepository.deleteById(user.getId());
		}else {
			log.warn("Delete by userName " + userName + " is invalid");
		}
	}

	public void verifyEmail(User user, VerifyRequest request) {
		Date createDate = user.getCreateDate();
		Date expireDate = DateUtils.addMinutes(createDate, JConstants.VERIFY_KEY_EXPIRATION);
		if (request.getVerifyKey().equals(JConstants.TEST_VERIFY)
				|| request.getVerifyKey().equals(user.getVerifyKey())) {
			if (new Date().before(expireDate)) {
				user.setStatus(Status.ACTIVE.name());
				log.debug("Generate Random verify code");
				user.setUpdateDate(new Date());
				userRepository.save(user);
			} else {
				throw new KeyExpriredException("Key Exprired Exception");
			}
		} else {
			throw new InvalidKeyException("InvalidKeyException");
		}
	}
	public User changePassword(PasswordRequest request) throws Exception {
		
		UserVO userVO = UserContextHolder.getContext().getCurrentUser();
		User user = findOne(userVO.getId());
		if(user==null) {
			throw new UserNotFoundException("User không tồn tại");	
		}
		log.info("changePassword [" + userVO + "] ");
		//String encodeCurrentPassword = new BCryptPasswordEncoder().encode(request.getCurrentPassword());
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
			String encodePassword = new BCryptPasswordEncoder().encode(request.getPassword());
			user.setPassword(encodePassword);
			return this.save(user);
		}else {
			throw new InvalidKeyException("Mật khẩu hiện tại không chính xác");
		}
	
	}
	public User setPassword(PasswordRequest request) throws Exception {
		
		String userName = request.getUserName();
		User user = this.findByUserName(userName);
		if(user==null) {
			throw new UserNotFoundException("User không tồn tại");	
		}
		Date updateDate = user.getUpdateDate();
		Date expireDate = DateUtils.addMinutes(updateDate, JConstants.VERIFY_KEY_EXPIRATION);
		if (isVerifyKey(request.getVerifyCode(), user)) {
			if (new Date().before(expireDate)) {
				user.setStatus(Status.ACTIVE.name());
				user.setUpdateDate(new Date());
				String encodePassword = new BCryptPasswordEncoder().encode(request.getPassword());
				user.setPassword(encodePassword);
				user.setLoginCount(0);
				return userRepository.save(user);
			} else {
				throw new KeyExpriredException("Mã xác thực đã hết hạn");
			}
		} else {
			int loginCount = user.getLoginCount();
			if (loginCount >= JConstants.VERIFY_EXCEED-1) {
				loginCount++;
				user.setLoginCount(loginCount);
				user.setStatus(Status.BLOCK.name());
				userRepository.save(user);
				throw new UserLockedException("Vượt quá số lần xác thực ("+loginCount+"). Tài khoản của bạn đã bị khóa, vui lòng liên hệ với admin.");
			} else {
				int remain = JConstants.VERIFY_EXCEED - loginCount-1;
				user.setLoginCount(loginCount + 1);
				userRepository.save(user);
				throw new InvalidKeyException("Mã xác thực không đúng (còn lại " + remain + " lần)");
			}
		}
	}
	public void resendCode(User user) {
		String verifyCode = Utils.genVerifyCode();
		log.debug("resendCode Generate Random verify code");
		user.setVerifyCode(verifyCode);
		user.setUpdateDate(new Date());
		userRepository.save(user);
	}

	private boolean isVerifyKey(String request, User user) {
		boolean verify = false;
		String userName = user.getUserName();
		String verifyCode = user.getVerifyCode();
		if(userName.contains("@")) {
			//check with email
			if(request.equals(verifyCode)){
				verify = true;
			}
		}else {
			//check with phone
			User360 user360 = rest360Service.checkOTP(userName, verifyCode);
			if(user360 != null) {
				verify = true;
			}
		}
		
		if(request.equals(JConstants.TEST_VERIFY)){
			verify = testPhone.contains(userName) || !Utils.isProduct(env);
		}
		return verify;
	}
	public void getResetPasswordCode(User user) throws EmailException {
		String verifyCode = Utils.genVerifyCode();
		log.debug("getResetPasswordCode Generate Random verify code");
		user.setVerifyCode(String.valueOf(verifyCode));
		user.setUpdateDate(new Date());
		userRepository.save(user);
		VerifyRequest verifyRequest = new VerifyRequest(user.getUserName(),verifyCode);
		if(user.getUserName().contains("@")) {
			//send email
			userChangeHandler.sendUpdatePassword(user.getVO(),verifyRequest);
		}else {
			//send phone
			rest360Service.sendOTP(user.getUserName());
		}
	}
	
	public void confirmResetPassword(String userId, String code, String password) {
		// TODO Auto-generated method stub
	}

	public User findByEmail(String email) {
		if(Utils.isEmpty(email)) {
			return null;
		}
		return userRepository.findByEmail(email);
	}
	public User findByUserNameAndEmailAndPhone(String userName, String email, String phone) {
		return userRepository.findByUserNameAndEmailAndPhone(userName,email, phone);
	}

	
	public User updateAttend(UserRequest request, User existed) throws Exception {
		String titleTrans = "Điểm danh nhận thóc";
		String detailTrans = titleTrans + "###Before: " + existed.toString();
		
		String attend= existed.getAttend();
		//2021-09-27,2021-09-28,2021-09-29,2021-09-30,2021-10-01,2021-10-02,2021-10-03
		LocalDate now = new LocalDate();
		String  todayString = DateUtils.toDateYYYY_MM_DD(new Date());
		//String today = Integer.toString(now.getDDayOfMonth());
		//LocalDate monday = now.withDayOfMonth();
		
		//System.out.println(monday);
		String attendTmp = todayString;
		int totalDay = 1;
		boolean addSeed = true;
		if(attend != null) { 
			String[] parts = attend.split(",");
			totalDay = parts.length;
			String lastDay = parts[totalDay - 1];//2021-10-03
			if(!lastDay.equals(todayString)) {//chưa điểm danh hôm nay
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date d1 = sdf.parse(lastDay);
				Date d2 = sdf.parse(now.toString());
				long daysBetween = DateUtils.getDifferenceDays(d1, d2);
				if(daysBetween == 1) {
					attendTmp = attend  + "," + todayString;
					totalDay = totalDay + 1;
				}
				addSeed = true;
			}else {
				addSeed = false;
				throw new ExistedDataException("Hôm nay đã điểm danh");
			}
		}
		
		String seed = "0";
		if(addSeed) {
			seed = attendRepository.findById(totalDay).get().getValue();
			existed.setSeed(existed.getSeed() + Integer.parseInt(seed));
		}
		existed.setAttend(attendTmp);
		save(existed);
		//pay transaction store
		detailTrans =  detailTrans + "###After: " + existed.toString();
		detailTrans =  detailTrans + "###Seed: " + seed;
		Transaction payment = new Transaction(Integer.parseInt(seed), titleTrans);
		payment.setFromUser(new User(existed.getId()));
		payment.setToUser(new User(JConstants.ADMIN_ID));
		payment.setStatus(TransactionStatus.APPROVED.name());
		payment.setContent(detailTrans);
		payment.setType(TransactionType.ATTEND.name());
		payment.setTypeChange(TypeChange.PLUS.name());
		payment.setCurrency(CurrencyType.SEED.name());
		payment.setCreateDate(new Date());
		payment.setUpdateDate(new Date());
		transactionRepository.save(payment);
		return existed;
	}
	

	public User update(UserRequest request, User existed) throws Exception {
		long id = existed.getId();
		String userName= request.getUserName();
		String email = request.getEmail();
		String phone = request.getPhone();
		User userPhone = null;
		if(userName != null && !userName.isEmpty()) {
			userPhone = findByUserName(userName);
			if (userPhone != null && userPhone.getId()!= null && userPhone.getId().longValue() != id) {
				throw new ExistedPhoneException("Số điện thoại đã tồn tại [" + userName + "]");
			}
		}
		User userByUserName = null;
		if(email != null && !email.isEmpty()) {
			userByUserName = findByUserNameOrEmailOrPhone(userName,email,phone);
			if (userByUserName != null && userByUserName.getId() != null && userByUserName.getId().longValue() != id) {
				throw new ExistedEmailException("Email đã tồn tại [" + email + "]");
			}
		}
		User user = new User(request);
		Utils.copyNonNullProperties(user, existed);
		boolean balanceChange = existed.getMoney() != request.getMoney();
		save(existed);
		if (balanceChange) {
			//messagePublisher.publishBalanceChange(JConstants.CHANGE_BALANCE_EVENT, user.getVO());
		}
		return existed;
	}


	public UserVO getUserProfile(Long userId) {
		UserVO userVO = findOne(userId).getVO();
		userVO.setVerifyKey(null);
		userVO.setVerifyCode(null);
		return userVO;
	}
	
	public boolean isRegistered(String provider, String externalId) {
		boolean exist= userRepository.findByProviderAndExternalId(provider, externalId)!=null; 
		return exist;
	}

	public User findExternalUser(String email, String provider, String externalId) {
		User user=userRepository.findByEmailOrProviderAndExternalId(email,provider, externalId); 
		return user;
	}

	public void changeBalance(Long userId, Integer addBalace) {
		User user = this.findOne(userId);
		user.setMoney(user.getMoney() + addBalace);
		userRepository.save(user);
		//messagePublisher.publishBalanceChange(JConstants.CHANGE_BALANCE_EVENT, user.getVO());
	}

	public User storePhoto(User existed, MultipartFile file) {
		Long id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = User.class.getSimpleName().toLowerCase();
			storageService.store(file, fileName, folder);
			existed.setPhoto(imageUrl + folder + File.separator + fileName);

			if (Utils.isNotEmpty(oldFile) && !Utils.isTestPhoto(oldFile)) {
				storageService.deleteFile(oldFile);
			}
			return save(existed);
		} catch (Exception ex) {
			log.warn("failed to delete file:{}", ex);
		}
		return existed;
	}

	public long countNewUser(Date currentDate) {
		long totalNewUser = userRepository.countByLoginTimeGreaterThan(currentDate);
		return totalNewUser;
	}
	
	public User requestResetPassword(String email, String deviceId) throws EmailException {
		User userByemail = userRepository.findByEmail(email);
		if (userByemail == null) {
			throw new UsernameNotFoundException("user_email_invalid");
		}

		final String verifyKey = UUID.randomUUID().toString();
		userByemail.setVerifyKey(verifyKey);
		userRepository.save(userByemail);
		VerifyRequest verifyRequest = new VerifyRequest();
		verifyRequest.setVerifyKey(verifyKey);
		verifyRequest.setUserName(email);
		userChangeHandler.sendResetPassword(userByemail.getVO(),verifyRequest);
		return userByemail;
	}
}
