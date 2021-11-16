package vn.vme.controller;
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
import vn.vme.common.JCode.UserCode;
import vn.vme.common.JConstants.CurrencyType;
import vn.vme.common.JConstants.Status;
import vn.vme.common.JConstants.TransactionStatus;
import vn.vme.common.JConstants.TransactionType;
import vn.vme.common.JConstants.TypeChange;
import vn.vme.common.URI;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Giftcode;
import vn.vme.entity.Pool;
import vn.vme.entity.Transaction;
import vn.vme.entity.User;
import vn.vme.entity.UserGiftcode;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.GiftcodeRequest;
import vn.vme.io.game.GiftcodeVO;
import vn.vme.io.game.UserGiftcodeRequest;
import vn.vme.io.game.UserGiftcodeVO;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.GiftcodeRepository;
import vn.vme.repository.PoolRepository;
import vn.vme.repository.TransactionRepository;
import vn.vme.repository.UserGiftcodeRepository;
import vn.vme.service.GiftcodeService;
import vn.vme.service.StorageService;
import vn.vme.service.UserService;

@RestController
@RequestMapping(URI.V1 + URI.GIFTCODE)
public class GiftcodeController extends BaseController {

	static Logger log = LoggerFactory.getLogger(GiftcodeController.class.getName());
	@Autowired
	public GiftcodeService giftcodeService;
	
	@Autowired(required = false)
	public UserService userService;
	
	@Autowired
	public GiftcodeRepository giftcodeRepository;
	
	@Autowired
	public PoolRepository poolRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	public UserGiftcodeRepository userGiftcodeRepository;
	
	@Autowired
	public StorageService storageService;

	@ApiOperation(value = "Register new giftcode")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Giftcode successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Giftcode already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Giftcode already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = GiftcodeVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity registerGiftcode(@RequestBody GiftcodeRequest request)
			throws Exception {
		log.info("Giftcode registration: " + request);
		super.validateRequest(request);
		Giftcode giftcode = giftcodeService.create(request);
		return response(giftcode.getVO());
	}
	
	@ApiOperation(value = "Create new general giftcode")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Giftcode successfully registered ", response = GiftcodeVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Giftcode already Registered with Phone Number and Domain name but not yet verify", response = GiftcodeVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Giftcode already registered also verified. Go signin.", response = GiftcodeVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = GiftcodeVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, GiftcodeName is used, as in the response", response = GiftcodeVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = GiftcodeVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createGiftcode(@RequestBody GiftcodeRequest request)
			throws Exception {
		log.info("Creating giftcode: " + request);
		Giftcode giftcode = giftcodeService.create(request);
		return response(giftcode.getVO());
	}
	
	@ApiOperation(value = "Update giftcode")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of giftcode update", response = GiftcodeVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateGiftcode(@ApiParam(value = "Object json giftcode") @RequestBody GiftcodeRequest request)
			throws Exception {

		Long id = request.getId();
		log.info("Update Giftcode:" + request);
		Giftcode existed = giftcodeService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Giftcode Id [" + id + "] invalid ");
		}
		
		existed = giftcodeService.update(request, existed);
		log.info("Patch Giftcode with existed = " + existed);
		return response(existed.getVO());
	}
	

	@ApiOperation(value = "Nhận quà Giftcode by random Id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return giftcode response  ", response = GiftcodeVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.USER , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity setGiftcodePool(@RequestBody GiftcodeRequest request) throws Exception {
		log.info("Get giftcode by POOL [" + request.getPoolId() + "]");
		this.validateUser();
		int poolId = request.getPoolId();
		UserVO user = this.getCurrentUser();
		Pageable paging = new Paging().getDefault();
		
		Integer eventId = 0;
		Integer gameId = 0;
		
		/*
		Pool existed = poolRepository.findById(request.getPoolId()).get();
		
		if(existed !=null && existed.getEvent() !=null) {
			eventId = existed.getEvent().getId();
		}
		if(existed !=null && existed.getGame() !=null) {
			gameId = existed.getGame().getId();
		}
		*/
		UserGiftcodeRequest req = new UserGiftcodeRequest("", 0L, gameId, eventId, user.getId(), 0, "", "");
		Page<UserGiftcode> result = userGiftcodeRepository.search(req, paging);
		log.info("Search giftcode existed [" + result.getTotalElements() + "]");
		
		if(result.getTotalElements()>0) {
			return response(UserCode.CODE_EXISTED.code, "Đã nhận quà pool [" + poolId + "]", null);
		}
		
		Giftcode giftcode = giftcodeRepository.findGiftcodeRandom(poolId);
		if (giftcode != null) {
			giftcode = giftcodeService.updateRandomGiftcodeGame(req, giftcode);
			return response(giftcodeService.load(giftcode.getVO()));
		} else {
			throw new NotFoundException("Giftcode POOLId [" + poolId + "] invalid ");
		}
	}
	
	@ApiOperation(value = "Update giftcode, check da dung chưa,  SINGLE update, khong doi trang thai, multi, all, sau do thi doi trang thai,")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Successful retrieval of giftcode update", response = GiftcodeVO.class),
		@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity useGiftcodeGame(@RequestBody UserGiftcodeRequest request)	throws Exception {
		this.validateUser();
		UserVO userVO  = UserContextHolder.getContext().getCurrentUser(); 
		log.info("useGiftcodeGame:" + userVO);
		User user = userService.findOne(userVO.getId());
		String code = request.getCode();
		
		Giftcode existedCode = giftcodeService.findByCode(code);
		if (existedCode == null) {
			throw new NotFoundException("Giftcode code [" + code + "] invalid ");
		}
		
		Pool pool = existedCode.getPool();
		String typeGC = pool.getType();
		String codeExited = UserCode.CODE_EXISTED.code;
		
		// check SINGLE, MULTI, ALL
		if(typeGC.equals("SINGLE")) {
			UserGiftcode userGiftcodeSingle = userGiftcodeRepository.findByGiftcodeId(existedCode.getId());
			if (userGiftcodeSingle != null) {
				return response(codeExited, "Mã code [" + code + "] chỉ được sử dụng 1 lần" , null);
			}
		}else if(typeGC.equals("ALL")) {
			UserGiftcode userGiftcodeAll = userGiftcodeRepository.findByUserIdAndGiftcodeId(userVO.getId(), existedCode.getId());
			if (userGiftcodeAll != null && userGiftcodeAll.getStatus().equals(Status.ACTIVE.name())) {
				throw new NotFoundException("Mã code [" + code + "] đã được sử dụng");
			}
		}else if(typeGC.equals("MULTI") ){
			UserGiftcode userGiftcodeMulti = userGiftcodeRepository.findByGiftcodeId(existedCode.getId());
			if (userGiftcodeMulti != null) {
				return response(codeExited, "Mã code [" + code + "] chỉ được sử dụng 1 lần" , null);
			}
		}else {
			throw new NotFoundException("Giftcode code [" + code + "] invalid ");
		}
		
		User existed = userService.findOne(userVO.getId());
		UserGiftcode userGiftcode = new UserGiftcode();
		userGiftcode.setGiftcode(existedCode);
		userGiftcode.setUser(existed);
		userGiftcode.setStatus(Status.ACTIVE.name());
		userGiftcodeRepository.save(userGiftcode);
		log.info("UseGiftcodeGame with existed = " + userGiftcode);
		
		Integer seed = pool.getSeed();
		if(seed > 0) {
			String titleTrans = "Giftcode "+ code +" nhận thóc";
			String detailTrans1 = titleTrans + "###Before: " + user.toString();
			if(user.getSeed() != null) {
				user.setSeed(user.getSeed() + seed);
			}else {
				user.setSeed(seed);
			}
			
			detailTrans1 =  detailTrans1 + "###After: " + user.toString();
			Transaction payment1 = new Transaction(seed, titleTrans);
			payment1.setFromUser(user);
			payment1.setToUser(user);
			payment1.setStatus(TransactionStatus.APPROVED.name());
			payment1.setContent(detailTrans1);
			payment1.setType(TransactionType.GIFTCODE.name());
			payment1.setTypeChange(TypeChange.PLUS.name());
			payment1.setCurrency(CurrencyType.SEED.name());
			payment1.setCreateDate(new Date());
			payment1.setUpdateDate(new Date());
			transactionRepository.save(payment1);
		}
		Integer rice = pool.getRice();
		if(rice > 0) {
			String titleTrans2 = "Giftcode "+ code +" nhận Gạo";
			String detailTrans2 = titleTrans2 + "###Before: " + user.toString();
			if(user.getRice() != null) {
				user.setRice(user.getRice() + rice);
			}else {
				user.setRice(rice);
			}
			detailTrans2 =  detailTrans2 + "###After: " + user.toString();
			Transaction payment2 = new Transaction(rice, titleTrans2);
			payment2.setFromUser(user);
			payment2.setToUser(user);
			payment2.setStatus(TransactionStatus.APPROVED.name());
			payment2.setContent(detailTrans2);
			payment2.setType(TransactionType.GIFTCODE.name());
			payment2.setTypeChange(TypeChange.PLUS.name());
			payment2.setCurrency(CurrencyType.RICE.name());
			payment2.setCreateDate(new Date());
			payment2.setUpdateDate(new Date());
			transactionRepository.save(payment2);
		}
		Integer exp = pool.getExpPoint();
		if(exp > 0) {
			String titleTrans3 = "Giftcode "+ code +" nhận EXP";
			String detailTrans3 = titleTrans3 + "###Before: " + user.toString();
			if(user.getExp() != null) {
				user.setExp(user.getExp() + exp);
			}else {
				user.setExp(exp);
			}
			
			detailTrans3 =  detailTrans3 + "###After: " + user.toString();
			Transaction payment3 = new Transaction(exp, titleTrans3);
			payment3.setFromUser(user);
			payment3.setToUser(user);
			payment3.setStatus(TransactionStatus.APPROVED.name());
			payment3.setContent(detailTrans3);
			payment3.setType(TransactionType.GIFTCODE.name());
			payment3.setTypeChange(TypeChange.PLUS.name());
			payment3.setCurrency(CurrencyType.EXP.name());
			payment3.setCreateDate(new Date());
			payment3.setUpdateDate(new Date());
			transactionRepository.save(payment3);
		}
		userService.save(user);
		return response(userGiftcode.getVO());
	}

	@ApiOperation(value = "Get Giftcode by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return giftcode response  ", response = GiftcodeVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getGiftcode(@ApiParam(value = "Get giftcode by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Long id) throws Exception {
		log.info("Get giftcode by id [" + id + "]");
		Giftcode giftcode = giftcodeService.findOne(id);
		if (giftcode != null) {
			return response(giftcodeService.load(giftcode.getVO()));
		} else {
			throw new NotFoundException("Giftcode Id [" + id + "] invalid ");
		}
	}
	
	@ApiOperation(value = "List giftcode by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = GiftcodeVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listGiftcode(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Integer eventId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Integer gameId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Integer poolId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Long userId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Giftcode");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		GiftcodeRequest request = new GiftcodeRequest(key, poolId, eventId, gameId, status, userId);
		Page<Giftcode> result = giftcodeRepository.search(request, paging);
		log.info("Search giftcode total elements [" + result.getTotalElements() + "]");
		List<Giftcode> contentList = result.getContent();
		List<GiftcodeVO> responseList = new ArrayList<GiftcodeVO>();

		for (Giftcode giftcode : contentList) {
			responseList.add(giftcodeService.load(giftcode.getVO()));
		}
		return responseList(responseList, result);
	}

	@ApiOperation(value = "List giftcode by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = GiftcodeVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listGiftcodeByUser(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") Integer eventId,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("List GiftcodeByUser");
		this.validateUser();
		UserVO user = this.getCurrentUser();
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		UserGiftcodeRequest request = new UserGiftcodeRequest("",0L, 0,eventId,user.getId(),0,"",status);
		Page<UserGiftcode> result = userGiftcodeRepository.search(request, paging);
		log.info("Search giftcode total elements [" + result.getTotalElements() + "]");
		List<UserGiftcode> contentList = result.getContent();
		List<UserGiftcodeVO> responseList = new ArrayList<UserGiftcodeVO>();

		for (UserGiftcode giftcode : contentList) {
			responseList.add(giftcode.getVO());
		}
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "Delete giftcode")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted giftcode", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Giftcode fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteGiftcode(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Long id) throws Exception {
		log.info("Delete  Giftcode [" + id + "]");
		Giftcode giftcode = giftcodeService.findOne(id);
		if (giftcode != null) {
			log.info("Delete  Giftcode [" + giftcode.getId() + "]");
			giftcodeService.delete(giftcode.getId());
			return responseMessage("Deleted  Giftcode [" + giftcode.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Giftcode id [" + id + "] invalid ");
		}
	}

	
}
