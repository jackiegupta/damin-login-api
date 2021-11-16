package vn.vme.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.DateUtils;
import vn.vme.common.URI;
import vn.vme.common.Utils;
import vn.vme.common.JConstants.Status;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Result;
import vn.vme.entity.Tour;
import vn.vme.entity.User;
import vn.vme.exception.ExistedDataException;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.ResultRequest;
import vn.vme.io.game.ResultVO;
import vn.vme.io.game.TourRequest;
import vn.vme.io.game.TourVO;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.ResultRepository;
import vn.vme.repository.TourRepository;
import vn.vme.repository.UserRepository;
import vn.vme.service.StorageService;
import vn.vme.service.TourService;

@RestController
@RequestMapping(URI.V1 + URI.TOUR)
public class TourController extends BaseController {

	static Logger log = LoggerFactory.getLogger(TourController.class.getName());

	@Autowired
	public TourService tourService;
	
	@Autowired
	public TourRepository tourRepository;
	
	@Autowired
	public ResultRepository resultRepository;
	
	@Autowired
	public UserRepository userRepository;
	
	
	@Autowired(required = false)
	public StorageService storageService;
	
	@Autowired
	Environment env;
	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Create new tour")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Tour create successfully  ", response = TourVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createTour(@ModelAttribute TourRequest request) throws Exception {
		log.info("Start createTour");
		super.validateRequest(request);
		Tour tour = tourService.create(request);
		if(tour != null) {
			tour = uploadFile(request, tour);
		}
		return response(tour.getVO());
		
	}
	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Create new tour")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Tour create successfully  ", response = ResultVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(value = URI.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity joinTour(@RequestBody ResultRequest request) throws Exception {
		log.info("Start createTour");
		super.validateRequest(request);
		UserVO currentUser = UserContextHolder.getContext().getCurrentUser();
		if(currentUser == null) {
			throw new NotFoundException("User Id invalid ");
		}
		Long userId = currentUser.getId();
		
		Integer tourId = request.getTourId();
		log.info("Update Tour:" + request);
		Tour tour = tourService.findOne(tourId);
		if (tour == null) {
			throw new NotFoundException("Tour Id [" + tourId + "] invalid ");
		}
		
		
		
		Result resultExisted = resultRepository.findByUserIdAndTourId(userId, tourId);
		if(resultExisted != null) {
			throw new ExistedDataException("Result đã tồn tại tourId:" + tourId);
		}
		
		Integer userCountJoin = resultRepository.countTour(tourId);
		if(userCountJoin > tour.getUserCount()) {
			throw new ExistedDataException("Hết slot tourId:" + tourId);
		}
		User user = userRepository.findById(currentUser.getId()).get();
		Result result = new Result(request);
		result.setUser(user);
		result.setCreateDate(new Date());
		result.setUpdateDate(new Date());
		result.setStatus(Status.ACTIVE.name());
		result = resultRepository.save(result);
		return response(result.getVO());
		
	}	
	
	@ApiOperation(value = "Update tour")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of tour update", response = TourVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updateTour(
			@ApiParam(value = "Object json tour") @ModelAttribute TourRequest request) throws Exception {
		
		Integer id = request.getId();
		log.info("Update Tour:" + request);
		Tour existed = tourService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Tour Id [" + id + "] invalid ");
		}
		
		Utils.copyNonNullProperties(request, existed);
		existed = tourService.save(existed);
		log.info("Patch Tour with existed = " + existed);
		existed = uploadFile(request, existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Update tour")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of tour update", response = TourVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(MediaType.APPLICATION_JSON_VALUE)
	@Scheduled(fixedRate = 1*60*1000)
	public ResponseEntity updateFinishTour() throws Exception {
		
		Pageable paging = new Paging().getPageRequest(1, 999, "", "");
		Page<Tour> result = tourRepository.findFinishTour(paging);
		log.info("Search tour total elements [" + result.getTotalElements() + "]");
		List<Tour> contentList = result.getContent();
		contentList.forEach(tour -> {
			TourRequest request = new TourRequest();
			request.setId(tour.getId());
			log.info("finish tourid: [" + tour.getId() + "]");
			tourService.finish(request);
		});
		return response();
	}
	
	private Tour uploadFile(TourRequest request, Tour tour) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			tour = tourService.storePhoto(tour,file);
		}
		return tour;
	}	
	
	@ApiOperation(value = "Finish Tour by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return tour response ", response = TourVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.LIVE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity finishTour(
			@ApiParam(value = "Object json tour")  @RequestBody TourRequest request) throws Exception {
		log.info("Finish Tour" + request);
		tourService.finish(request);
		return response("OK");
	}
	
	@ApiOperation(value = "Get Tour by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return tour response ", response = TourVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getTour(
			@ApiParam(value = "Get tour by Id", defaultValue = "1") @PathVariable Integer id) throws Exception {
		log.info("Get detail Tour");

		Tour tour = tourService.findOne(id);
		log.info("Get tour by getId [" + id + "]");
		if (tour != null) {
			TourVO tourVO = tourService.getDetail(tour);
			return response(tourVO);
		} else {
			throw new NotFoundException("Tour Id [" + id + "] invalid ");
		}
	}
	
	@ApiOperation(value = "List my tour by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = TourVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listUserTour(
			@RequestParam(required = false, defaultValue = "1") Long userId, 
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "createDate") String sortProperty,
			@RequestParam(required = false, defaultValue = "true") Boolean follow
			) throws Exception {
		log.info("Search Tour of userId " + userId);
		UserVO currentUser = UserContextHolder.getContext().getCurrentUser();
		if(Utils.isNotEmpty(currentUser)) {
			userId = currentUser.getId();
		}
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		Page<Tour> result = null;
		//if(follow == null) {
			result = tourService.findByUserId(userId, paging);
		//}else {
			//result = tourService.findByUserIdAndFollow(userId, follow, paging);
		//}
		log.info("Search tour total elements [" + result.getTotalElements() + "]");
		List<Tour> contentList = result.getContent();
		List<TourVO> responseList = new ArrayList<TourVO>();
		
		contentList.forEach(tour -> {
			TourVO tourVO = tourService.getDetail(tour);
			responseList.add(tourVO);
		});
		
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "List tour by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = TourVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listTour(
			@RequestParam(required = false, defaultValue = "") String key,
			@RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "0") Long fromPrice,
			@RequestParam(required = false, defaultValue = "0") Long toPrice,
			@RequestParam(required = false, defaultValue = "") String fromDate,
			@RequestParam(required = false, defaultValue = "") String toDate,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Tour");
		if (!fromDate.contains("undefined--") && Utils.isNotEmpty(fromDate) && DateUtils.toDateYYYYMMDD(fromDate) != null) {
			fromDate = fromDate + " 00:00:00";
		}else {
			fromDate = "";
		}
		if (!toDate.contains("undefined--") && Utils.isNotEmpty(toDate) && DateUtils.toDateYYYYMMDD(toDate) != null) {
			toDate = toDate + " 00:00:00";
		}else {
			toDate = "";
		}
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		TourRequest request = new TourRequest(key, fromPrice, toPrice, fromDate, toDate, status);
		Page<Tour> result = tourRepository.search(request, paging);
		//Page<Tour> result = tourService.findByAdminId(1L, paging);
		log.info("Search tour total elements [" + result.getTotalElements() + "]");
		List<Tour> contentList = result.getContent();
		List<TourVO> responseList = new ArrayList<TourVO>();
		
		contentList.forEach(tour -> {
			responseList.add(tourService.getDetail(tour));
		});
		
		return responseList(responseList, result);
	}

	@ApiOperation(value = "Update user avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of user update", response = UserVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity uploadPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json user") @RequestParam(value = "id", required = true) Integer id)
			throws Exception {

		log.info("uploadPhoto:" + id);
		
		Tour existed = tourService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Tour Id [" + id + "] invalid ");
		}
		
		 if (file != null) {
			 try {
				 
				 if (file != null) {
						existed = tourService.storePhoto(existed,file);
					}
				String oldFile = existed.getPhoto();
				String fileName = id + "_" + UUID.randomUUID() + "."
						+ FilenameUtils.getExtension(file.getOriginalFilename());
				String folder = Tour.class.getSimpleName().toLowerCase();
						storageService.store(file, fileName, folder);
				 existed.setPhoto(fileName);
				 existed = tourService.save(existed);
				 log.info("uploadPhoto = " + existed);
				if (oldFile != null && !oldFile.isEmpty() && !oldFile.equals("mc.jpg")) {
                     storageService.deleteFile(oldFile);
                }
             } catch (Exception ex) {
                 log.warn("failed to delete file:{}", ex);
             }
         }
		
		return response();
	}
	@ApiOperation(value = "Delete tour")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted tour", response = Response.class),
			@ApiResponse(code = 201, message = "STARTED, Already registered also verified with Phone Number and Tour name. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteTour(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Tour [" + id + "]");
		Tour tour = tourService.findOne(id);
		if (tour != null) {
			log.info("Delete  Tour [" + id + "]");
			tourService.delete(id);
			return responseMessage("Deleted  Tour [" + id + "] sucessfully");
		} else {
			throw new NotFoundException("Tour Id [" + id + "] invalid ");
		}
	}
	
}
