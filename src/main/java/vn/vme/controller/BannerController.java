package vn.vme.controller;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.URI;
import vn.vme.entity.Banner;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.BannerRequest;
import vn.vme.io.game.BannerVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.BannerRepository;
import vn.vme.service.BannerService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.BANNER)
public class BannerController extends BaseController {

	static Logger log = LoggerFactory.getLogger(BannerController.class.getName());
	@Autowired
	public BannerService bannerService;
	
	@Autowired
	public BannerRepository bannerRepository;
	@Autowired
	public StorageService storageService;

	@ApiOperation(value = "Register new banner")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Banner successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Banner already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Banner already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = BannerVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity registerBanner(@ModelAttribute BannerRequest request)
			throws Exception {
		log.info("Banner registration: " + request);
		super.validateRequest(request);
		Banner banner = bannerService.create(request);
		if(banner != null) {
			banner = uploadFile(request, banner);
		}
		return response(banner.getVO());
	}
	
	@ApiOperation(value = "Create new general banner")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Banner successfully registered ", response = BannerVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Banner already Registered with Phone Number and Domain name but not yet verify", response = BannerVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Banner already registered also verified. Go signin.", response = BannerVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = BannerVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, BannerName is used, as in the response", response = BannerVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = BannerVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createBanner(@ModelAttribute BannerRequest request)
			throws Exception {
		log.info("Creating banner: " + request);
		Banner banner = bannerService.create(request);
		if(banner != null) {
			banner = uploadFile(request, banner);
		}
		return response(banner.getVO());
	}
	
	@ApiOperation(value = "Update banner")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of banner update", response = BannerVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updateBanner(@ApiParam(value = "Object json banner") @ModelAttribute BannerRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Banner:" + request);
		Banner existed = bannerService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Banner Id [" + id + "] invalid ");
		}
		
		existed = bannerService.update(request, existed);
		existed = uploadFile(request, existed);
		log.info("Patch Banner with existed = " + existed);
		return response(existed.getVO());
	}
	
	private Banner uploadFile(BannerRequest request, Banner banner) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			banner = bannerService.storePhoto(banner,file);
		}
		return banner;
	}	
	
	@ApiOperation(value = "Update gameType avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of gameType update", response = BannerVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateBannerPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json gameType") @RequestParam(value = "id", required = true) Integer id)
			throws Exception {
		log.info("Upload photo:" + id);
		Banner existed = bannerService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Banner Id [" + id + "] invalid ");
		}

		if (file != null) {
			existed = bannerService.storePhoto(existed,file);
		}
		existed = bannerService.save(existed);
		log.info("Patch Banner with existed = " + existed);
		return response(existed.getVO());
	}

	@ApiOperation(value = "Get Banner by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return banner response  ", response = BannerVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getBanner(@ApiParam(value = "Get banner by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get banner by id [" + id + "]");
		Banner banner = bannerService.findOne(id);
		if (banner != null) {
			return response(bannerService.load(banner.getVO()));
		} else {
			throw new NotFoundException("Banner Id [" + id + "] invalid ");
		}
	}
	@ApiOperation(value = "List banner by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = BannerVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listBanner(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Banner");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		BannerRequest request = new BannerRequest(key,status);
		Page<Banner> result = bannerRepository.search(request, paging);
		log.info("Search banner total elements [" + result.getTotalElements() + "]");
		List<Banner> contentList = result.getContent();
		List<BannerVO> responseList = new ArrayList<BannerVO>();

		for (Banner banner : contentList) {
			responseList.add(bannerService.load(banner.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete banner")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted banner", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Banner fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteBanner(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Banner [" + id + "]");
		Banner banner = bannerService.findOne(id);
		if (banner != null) {
			log.info("Delete  Banner [" + banner.getId() + "]");
			bannerService.delete(banner.getId());
			return responseMessage("Deleted  Banner [" + banner.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Banner id [" + id + "] invalid ");
		}
	}

	
}
