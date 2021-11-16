package vn.vme.controller;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.URI;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Bonus;
import vn.vme.entity.User;
import vn.vme.entity.UserBonus;
import vn.vme.exception.ExistedDataException;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.BonusRequest;
import vn.vme.io.game.BonusVO;
import vn.vme.io.game.UserBonusRequest;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.BonusRepository;
import vn.vme.repository.UserBonusRepository;
import vn.vme.service.BonusService;
import vn.vme.service.StorageService;
import vn.vme.service.UserService;

@RestController
@RequestMapping(URI.V1 + URI.BONUS)
public class BonusController extends BaseController {

	static Logger log = LoggerFactory.getLogger(BonusController.class.getName());
	@Autowired
	public BonusService bonusService;
	
	@Autowired
	public BonusRepository bonusRepository;
	
	@Autowired
	public UserBonusRepository userBonusRepository;
	
	
	@Autowired
	public StorageService storageService;
	
	@Autowired(required = false)
	public UserService userService;

	
	@ApiOperation(value = "Create new general bonus")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Bonus successfully registered ", response = BonusVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Bonus already Registered with Phone Number and Domain name but not yet verify", response = BonusVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Bonus already registered also verified. Go signin.", response = BonusVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = BonusVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, BonusName is used, as in the response", response = BonusVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = BonusVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createBonus(@ModelAttribute BonusRequest request)
			throws Exception {
		log.info("Creating bonus: " + request);
		Bonus bonus = bonusService.create(request);
		if(bonus != null) {
			bonus = uploadFile(request, bonus);
		}
		return response(bonus.getVO());
	}
	
	@ApiOperation(value = "Update bonus")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of bonus update", response = BonusVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateBonus(@ApiParam(value = "Object json bonus") @ModelAttribute BonusRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Bonus:" + request);
		Bonus existed = bonusService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Bonus Id [" + id + "] invalid ");
		}
		
		existed = bonusService.update(request, existed);
		existed = uploadFile(request, existed);
		log.info("Patch Bonus with existed = " + existed);
		return response(existed.getVO());
	}
	
	private Bonus uploadFile(BonusRequest request, Bonus bonus) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			bonus = bonusService.storePhoto(bonus,file);
		}
		return bonus;
	}	

	@ApiOperation(value = "Get Bonus by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return bonus response  ", response = BonusVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getBonus(@ApiParam(value = "Get bonus by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get bonus by id [" + id + "]");
		Bonus bonus = bonusService.findOne(id);
		if (bonus != null) {
			return response(bonusService.load(bonus.getVO()));
		} else {
			throw new NotFoundException("Bonus Id [" + id + "] invalid ");
		}
	}
	
	@ApiOperation(value = "Get Bonus by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return task response  ", response = BonusVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.CHECK, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity checkBonus(@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Integer bonusId) throws Exception {
		
		log.info("Get Bonus by id [" + bonusId + "]");
		
		UserVO userVO  = UserContextHolder.getContext().getCurrentUser(); 
		log.info("Update User:" + userVO);
		User existed = userService.findOne(userVO.getId());
		if (existed == null) {
			throw new NotFoundException("User Id [" + userVO.getId() + "] invalid ");
		}
		UserBonus bonus = userBonusRepository.getBonus(userVO.getId(), bonusId);
		if (bonus != null) {
			return response(bonus.getVO());
		} else {
			throw new NotFoundException("Bonus Id [" + bonusId + "] invalid ");
		}
	}
	@ApiOperation(value = "Get Bonus by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return task response  ", response = BonusVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.CHECK, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity receivingGift(
			@RequestBody UserBonusRequest request
			) throws Exception {
		Integer bonusId = request.getBonusId();
		log.info("Get Bonus by id [" + bonusId + "]");
		
		UserVO userVO  = UserContextHolder.getContext().getCurrentUser(); 
		log.info("Update User:" + userVO);
		User existed = userService.findOne(userVO.getId());
		if (existed == null) {
			throw new NotFoundException("User Id [" + userVO.getId() + "] invalid ");
		}
		UserBonus userBonus = userBonusRepository.getBonus(userVO.getId(),bonusId);
		if (userBonus != null) {
			throw new ExistedDataException("Existed Data Bonus Id [" + bonusId + "]");
		} else {
			Bonus bonus = bonusService.findOne(bonusId);
			if(bonus != null) {
				UserBonus userBonusSave = new UserBonus();
				userBonusSave.setUser(existed);
				userBonusSave.setBonus(bonus);
				bonusService.saveUserBonus(userBonusSave);
				return response(userBonusSave.getVO());
			}else {
				throw new NotFoundException("Bonus Id [" + bonusId + "] invalid ");
			}
		}
	}

	
	@ApiOperation(value = "List bonus by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = BonusVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listBonus(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "0") int levelId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String scope,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Bonus");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		BonusRequest request = new BonusRequest(key, levelId,scope, status);
		Page<Bonus> result = bonusRepository.search(request, paging);
		log.info("Search bonus total elements [" + result.getTotalElements() + "]");
		List<Bonus> contentList = result.getContent();
		List<BonusVO> responseList = new ArrayList<BonusVO>();

		for (Bonus bonus : contentList) {
			responseList.add(bonusService.load(bonus.getVO()));
		}
		return responseList(responseList, result);
	}
	
	
	@ApiOperation(value = "Delete bonus")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted bonus", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Bonus fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteBonus(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Bonus [" + id + "]");
		Bonus bonus = bonusService.findOne(id);
		if (bonus != null) {
			log.info("Delete  Bonus [" + bonus.getId() + "]");
			bonusService.delete(bonus.getId());
			return responseMessage("Deleted  Bonus [" + bonus.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Bonus id [" + id + "] invalid ");
		}
	}

	
}
