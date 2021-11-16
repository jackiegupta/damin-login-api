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

import vn.vme.common.JConstants.CurrencyType;
import vn.vme.common.JConstants.Status;
import vn.vme.common.JConstants.TransactionStatus;
import vn.vme.common.JConstants.TransactionType;
import vn.vme.common.JConstants.TypeChange;
import vn.vme.common.JConstants;
import vn.vme.common.Utils;
import vn.vme.entity.Pool;
import vn.vme.entity.Task;
import vn.vme.entity.Transaction;
import vn.vme.entity.User;
import vn.vme.io.finance.TransactionRequest;
import vn.vme.io.finance.TransactionVO;
import vn.vme.repository.TransactionRepository;

@Service
public class TransactionService {
	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

	@Autowired
	private TransactionRepository transactionRepository;
	
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
	public TransactionService() {
	}
	
	public TransactionVO load(TransactionVO vo) {
		return vo;
	}
	public Transaction findOne(Long id) {
		return transactionRepository.findById(id).get();
	}
	public Page<Transaction> search(TransactionRequest request, Pageable pageable) {
		
		return transactionRepository.search(request, pageable);
	}
	
	public Iterable<Transaction> findAll() {
		return transactionRepository.findAll();
	}
	public Transaction create(TransactionRequest request) {
		request.setStatus(Status.ACTIVE.name());
		Transaction transaction = new Transaction(request);
		transaction.setCreateDate(new Date());
		return this.save(transaction);
	}
	
	public Transaction update(TransactionRequest request, Transaction existed) throws Exception {
		log.debug("update " + request);
		Transaction transaction = new Transaction(request);
		Utils.copyNonNullProperties(transaction, existed);
		existed = save(existed);
		return existed;
	}

	public Transaction save(Transaction transaction) {
		log.debug("save " + transaction);
		Long id = transaction.getId();
		transaction = transactionRepository.save(transaction);
		log.debug("save " + id);
		return transaction;
	}
	
	public void delete(Long id) {
		transactionRepository.deleteById(id);
	}

	public void saveTask(Task task, User user) {
		//if task.seed > 0
		Integer seed = task.getSeed();
		if(seed > 0) {
			String titleTransSeed = "Nhiệm vụ " + task.getCode() + " nhận thóc";
			String detailTransSeed = titleTransSeed + "###Before: " + user.toString();
			user.setSeed(user.getSeed() + seed);
			try {
				userService.save(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//pay transaction store
			detailTransSeed =  detailTransSeed + "###After: " + user.toString();
			Transaction paymentSeed = new Transaction(seed, titleTransSeed);
			paymentSeed.setFromUser(new User(user.getId()));
			paymentSeed.setToUser(new User(JConstants.ADMIN_ID));
			paymentSeed.setStatus(TransactionStatus.APPROVED.name());
			paymentSeed.setContent(detailTransSeed);
			paymentSeed.setType(TransactionType.TASK.name());
			paymentSeed.setTypeChange(TypeChange.PLUS.name());
			paymentSeed.setCurrency(CurrencyType.SEED.name());
			paymentSeed.setCreateDate(new Date());
			paymentSeed.setUpdateDate(new Date());
			transactionRepository.save(paymentSeed);
		}
		
		//if task.rice > 0
		Integer rice = task.getRice();
		if(rice > 0) {
			String titleTransRice = "Nhiệm vụ " + task.getCode() + " nhận Gạo";
			String detailTransRice = titleTransRice + "###Before: " + user.toString();
			user.setRice(user.getRice() + rice);
			try {
				userService.save(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//pay transaction store
			detailTransRice =  detailTransRice + "###After: " + user.toString();
			Transaction paymentRice = new Transaction(seed, titleTransRice);
			paymentRice.setFromUser(new User(user.getId()));
			paymentRice.setToUser(new User(JConstants.ADMIN_ID));
			paymentRice.setStatus(TransactionStatus.APPROVED.name());
			paymentRice.setContent(detailTransRice);
			paymentRice.setType(TransactionType.TASK.name());
			paymentRice.setTypeChange(TypeChange.PLUS.name());
			paymentRice.setCurrency(CurrencyType.RICE.name());
			paymentRice.setCreateDate(new Date());
			paymentRice.setUpdateDate(new Date());
			transactionRepository.save(paymentRice);
		}
		
		//if task.exp > 0
		Integer exp = task.getExpPoint();
		if(exp > 0) {
			String titleTransExp = "Giftcode nhận Exp";
			String detailTransExp = titleTransExp + "###Before: " + user.toString();
			user.setExp(user.getExp() + exp);
			try {
				userService.save(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//pay transaction store
			detailTransExp =  detailTransExp + "###After: " + user.toString();
			Transaction paymentExp = new Transaction(seed, titleTransExp);
			paymentExp.setFromUser(new User(user.getId()));
			paymentExp.setToUser(new User(JConstants.ADMIN_ID));
			paymentExp.setStatus(TransactionStatus.APPROVED.name());
			paymentExp.setContent(detailTransExp);
			paymentExp.setType(TransactionType.TASK.name());
			paymentExp.setTypeChange(TypeChange.PLUS.name());
			paymentExp.setCurrency(CurrencyType.POINT.name());
			paymentExp.setCreateDate(new Date());
			paymentExp.setUpdateDate(new Date());
			transactionRepository.save(paymentExp);
		}
		
	}

	public void savePool(Pool pool, User user) {
		//if code pool.seed > 0
		Integer seed = pool.getSeed();
		if(seed > 0) {
			String titleTransSeed = "Giftcode nhận thóc";
			String detailTransSeed = titleTransSeed + "###Before: " + user.toString();
			user.setSeed(user.getSeed() + seed);
			try {
				userService.save(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//pay transaction store
			detailTransSeed =  detailTransSeed + "###After: " + user.toString();
			Transaction paymentSeed = new Transaction(seed, titleTransSeed);
			paymentSeed.setFromUser(new User(user.getId()));
			paymentSeed.setToUser(new User(JConstants.ADMIN_ID));
			paymentSeed.setStatus(TransactionStatus.APPROVED.name());
			paymentSeed.setContent(detailTransSeed);
			paymentSeed.setType(TransactionType.GIFTCODE.name());
			paymentSeed.setTypeChange(TypeChange.PLUS.name());
			paymentSeed.setCurrency(CurrencyType.SEED.name());
			paymentSeed.setCreateDate(new Date());
			paymentSeed.setUpdateDate(new Date());
			transactionRepository.save(paymentSeed);
		}
		
		//if code pool.rice > 0
		Integer rice = pool.getRice();
		if(rice > 0) {
			String titleTransRice = "Giftcode nhận gạo";
			String detailTransRice = titleTransRice + "###Before: " + user.toString();
			user.setRice(user.getRice() + rice);
			try {
				userService.save(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//pay transaction store
			detailTransRice =  detailTransRice + "###After: " + user.toString();
			Transaction paymentRice = new Transaction(seed, titleTransRice);
			paymentRice.setFromUser(new User(user.getId()));
			paymentRice.setToUser(new User(JConstants.ADMIN_ID));
			paymentRice.setStatus(TransactionStatus.APPROVED.name());
			paymentRice.setContent(detailTransRice);
			paymentRice.setType(TransactionType.GIFTCODE.name());
			paymentRice.setTypeChange(TypeChange.PLUS.name());
			paymentRice.setCurrency(CurrencyType.RICE.name());
			paymentRice.setCreateDate(new Date());
			paymentRice.setUpdateDate(new Date());
			transactionRepository.save(paymentRice);
		}
		
		//if code pool.exp > 0
		Integer exp = pool.getExpPoint();
		if(exp > 0) {
			String titleTransExp = "Giftcode nhận Exp";
			String detailTransExp = titleTransExp + "###Before: " + user.toString();
			user.setExp(user.getExp() + exp);
			try {
				userService.save(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//pay transaction store
			detailTransExp =  detailTransExp + "###After: " + user.toString();
			Transaction paymentExp = new Transaction(seed, titleTransExp);
			paymentExp.setFromUser(new User(user.getId()));
			paymentExp.setToUser(new User(JConstants.ADMIN_ID));
			paymentExp.setStatus(TransactionStatus.APPROVED.name());
			paymentExp.setContent(detailTransExp);
			paymentExp.setType(TransactionType.GIFTCODE.name());
			paymentExp.setTypeChange(TypeChange.PLUS.name());
			paymentExp.setCurrency(CurrencyType.POINT.name());
			paymentExp.setCreateDate(new Date());
			paymentExp.setUpdateDate(new Date());
			transactionRepository.save(paymentExp);
		}
		
		//if code pool.items > 0
		String items = pool.getItems();//format ID-Quantity: 1-3,2-1
		if(items != null) {
			
			
		}
		
	}
}
