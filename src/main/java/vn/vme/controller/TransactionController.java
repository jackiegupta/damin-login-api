package vn.vme.controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.JConstants.Status;
import vn.vme.common.JConstants.TransactionStatus;
import vn.vme.common.JConstants.TransactionType;
import vn.vme.common.JConstants.TypeChange;
import vn.vme.common.JConstants.UserLevel;
import vn.vme.common.URI;
import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Payment;
import vn.vme.entity.Product;
import vn.vme.entity.Setting;
import vn.vme.entity.Task;
import vn.vme.entity.Transaction;
import vn.vme.entity.User;
import vn.vme.entity.UserTask;
import vn.vme.exception.NotFoundException;
import vn.vme.io.finance.TransactionRequest;
import vn.vme.io.finance.TransactionVO;
import vn.vme.io.finance.VitapayTransactionRequest;
import vn.vme.io.game.UserTaskRequest;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.TransactionRepository;
import vn.vme.repository.UserTaskRepository;
import vn.vme.service.PaymentService;
import vn.vme.service.ProductService;
import vn.vme.service.SettingService;
import vn.vme.service.StorageService;
import vn.vme.service.TaskService;
import vn.vme.service.TransactionService;
import vn.vme.service.UserService;

@RestController
@RequestMapping(URI.V1 + URI.TRANSACTION)
public class TransactionController extends BaseController {

	static Logger log = LoggerFactory.getLogger(TransactionController.class.getName());
	@Autowired
	public TransactionService transactionService;
	
	@Autowired
	public TransactionRepository transactionRepository;
	
	@Autowired(required = false)
	public TaskService taskService;
	
	@Autowired
    public UserTaskRepository userTaskRepository;
	
	@Autowired
	public TransactionRepository userTransactionRepository;
	
	@Autowired
	public StorageService storageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	public PaymentService paymentService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	public SettingService settingService;
	

	@ApiOperation(value = "Register new transaction")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Transaction successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Transaction already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Transaction already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = TransactionVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity registerTransaction(@RequestBody TransactionRequest request)
			throws Exception {
		log.info("Transaction registration: " + request);
		super.validateRequest(request);
		Transaction transaction = transactionService.create(request);
		return response(transaction.getVO());
	}
	
	@ApiOperation(value = "Create new general transaction")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Transaction successfully registered ", response = TransactionVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Transaction already Registered with Phone Number and Domain name but not yet verify", response = TransactionVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Transaction already registered also verified. Go signin.", response = TransactionVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = TransactionVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, TransactionName is used, as in the response", response = TransactionVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = TransactionVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.VITAPAY, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createTransactionVitaPay(@RequestBody VitapayTransactionRequest request)
			throws Exception {
		log.info("Creating createTransactionVitaPay: " + request);
		
		UserVO userVO = UserContextHolder.getContext().getCurrentUser();
		
		User user = userService.findOne(userVO.getId());
		
		String refId = request.getRefId();
		Payment payment = paymentService.findByRefId(refId);
		if (payment == null) {
			throw new NotFoundException("Payment refId [" + refId + "] NotFound ");
		}
		if (payment.getStatus().equals(Status.ACTIVE.name())) {
			throw new NotFoundException("Payment refId [" + refId + "] Done ");
		}
		
		if (user == null) {
			throw new NotFoundException("User [" + refId + "] NotFound ");
		}
		
		Product product = payment.getProduct();
		
		if (product == null) {
			throw new NotFoundException("Product NotFound");
		}
		
		//update Payment
		payment.setStatus(Status.ACTIVE.name());
		payment.setUpdateDate(new Date());
		paymentService.save(payment);
		
		Integer money = product.getPrice();
		Integer rice = product.getAmount();
		Integer vipPoint = product.getExp();
		TransactionRequest transactionRequest = new TransactionRequest();
		
		String content = Utils.convertObjectToJSon(request);
		String titleTransRice = "Nạp gạo";
		String detailTransRice = titleTransRice + "###Before: " + user.toString();
		user.setMoney(user.getMoney() + money);
		user.setRice(user.getRice() + rice);
		int newPoint = user.getPoint() + vipPoint;
		user.setPoint(newPoint);
		
		try {
			//Check hang theo diem
			//11 BRONZE
			//12 SILVER
			//13 GOLD
			//14 PLATIN
			
			Setting BRONZE = settingService.findOne(11);
			Setting SILVER = settingService.findOne(12);
			Setting GOLD = settingService.findOne(13);
			Setting PLATIN = settingService.findOne(14);
			if(newPoint >= Integer.parseInt(BRONZE.getValue())) {
				user.setLevel(UserLevel.BRONZE.name());
			}
			if(newPoint >= Integer.parseInt(SILVER.getValue())) {
				user.setLevel(UserLevel.SILVER.name());
			}
			if(newPoint >= Integer.parseInt(GOLD.getValue())) {
				user.setLevel(UserLevel.GOLD.name());
			}
			if(newPoint >= Integer.parseInt(PLATIN.getValue())) {
				user.setLevel(UserLevel.PLATIN.name());
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		userService.save(user);
		//pay transaction store
		detailTransRice =  detailTransRice + "###After: " + user.toString();
		
		transactionRequest.setTitle(titleTransRice);
		transactionRequest.setAmount(rice);
		transactionRequest.setFromUser(user);
		transactionRequest.setToUser(user);
		transactionRequest.setStatus(TransactionStatus.APPROVED.name());
		transactionRequest.setContent(content);
		transactionRequest.setType(TransactionType.TRANSFER.name());
		transactionRequest.setTypeChange(TypeChange.PLUS.name());
		transactionRequest.setCurrency(product.getCurrency());
		
		Transaction transaction = transactionService.create(transactionRequest);
		
		try {
            //TASK RECHARGE
            Task task = taskService.findByCode("RECHARGE");
            if(task != null) {
            	UserTaskRequest userTaskRequest = new UserTaskRequest("", user.getId(), 0, 0 ,task.getId(), "", "", "");
            	Date date = new Date();
            	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");//05-02-2020
            	String datetime = formatter.format(date);
            	userTaskRequest.setUpdateDate(datetime + " 00:00:00");
            	List<UserTask> list = userTaskRepository.findUserTask(userTaskRequest);
            	if(list !=null && list.size() >= 0) {
            		log.info("có TASK RECHARGE");
            	}else {
            		log.info("Chưa có TASK RECHARGE");
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
		
		
		return response(transaction.getVO());
	}
	
	@ApiOperation(value = "Create new general transaction")
	@ApiResponses({
		@ApiResponse(code = 201, message = "CREATED, Transaction successfully registered ", response = TransactionVO.class),
		@ApiResponse(code = 202, message = "ACCEPTED, Transaction already Registered with Phone Number and Domain name but not yet verify", response = TransactionVO.class),
		@ApiResponse(code = 208, message = "ALREADY_REPORTED, Transaction already registered also verified. Go signin.", response = TransactionVO.class),
		@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = TransactionVO.class),
		@ApiResponse(code = 409, message = "CONFLICT, TransactionName is used, as in the response", response = TransactionVO.class),
		@ApiResponse(code = 423, message = "LOCKED", response = TransactionVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createTransaction(@RequestBody TransactionRequest request)
			throws Exception {
		log.info("Creating transaction: " + request);
		Transaction transaction = transactionService.create(request);
		return response(transaction.getVO());
	}
	
	@ApiOperation(value = "Update transaction")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of transaction update", response = TransactionVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updateTransaction(@ApiParam(value = "Object json transaction") @RequestBody TransactionRequest request)
			throws Exception {

		Long id = request.getId();
		log.info("Update Transaction:" + request);
		Transaction existed = transactionService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Transaction Id [" + id + "] invalid ");
		}
		
		existed = transactionService.update(request, existed);
		log.info("Patch Transaction with existed = " + existed);
		return response(existed.getVO());
	}

	@ApiOperation(value = "Get Transaction by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return transaction response  ", response = TransactionVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getTransaction(@ApiParam(value = "Get transaction by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Long id) throws Exception {
		log.info("Get transaction by id [" + id + "]");
		Transaction transaction = transactionService.findOne(id);
		if (transaction != null) {
			return response(transactionService.load(transaction.getVO()));
		} else {
			throw new NotFoundException("Transaction Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "Get Transaction by user id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return transaction response  ", response = TransactionVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listTransaction(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Long fromUsertId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String currency,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String typeChange,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		UserVO user = UserContextHolder.getContext().getCurrentUser();
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		TransactionRequest request = new TransactionRequest(key, user.getId(), 0L, status, "", "");
		request.setType(type);
		request.setTypeChange(typeChange);
		request.setCurrency(currency);
		Page<Transaction> result = userTransactionRepository.search(request, paging);
		log.info("Search transaction total elements [" + result.getTotalElements() + "]");
		List<Transaction> contentList = result.getContent();
		List<TransactionVO> responseList = new ArrayList<TransactionVO>();

		for (Transaction userTransaction : contentList) {
			responseList.add(transactionService.load(userTransaction.getVO()));
		}
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "List transaction by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = TransactionVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listTransaction(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Long userId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Long toUserId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Transaction");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		TransactionRequest request = new TransactionRequest(key,userId, toUserId,status, "","");
		Page<Transaction> result = transactionRepository.search(request, paging);
		log.info("Search transaction total elements [" + result.getTotalElements() + "]");
		List<Transaction> contentList = result.getContent();
		List<TransactionVO> responseList = new ArrayList<TransactionVO>();

		for (Transaction transaction : contentList) {
			responseList.add(transactionService.load(transaction.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete transaction")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted transaction", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Transaction fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteTransaction(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Long id) throws Exception {
		log.info("Delete  Transaction [" + id + "]");
		Transaction transaction = transactionService.findOne(id);
		if (transaction != null) {
			log.info("Delete  Transaction [" + transaction.getId() + "]");
			transactionService.delete(transaction.getId());
			return responseMessage("Deleted  Transaction [" + transaction.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Transaction id [" + id + "] invalid ");
		}
	}

	
}
