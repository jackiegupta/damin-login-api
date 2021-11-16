package vn.vme.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import vn.vme.common.Utils;
import vn.vme.entity.Support;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.SupportRequest;
import vn.vme.io.game.SupportVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.SupportRepository;
import vn.vme.service.SupportService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.SUPPORT)
public class SupportController extends BaseController {

	static Logger log = LoggerFactory.getLogger(SupportController.class.getName());

	@Autowired
	public SupportService supportService;
	
	@Autowired
	public SupportRepository supportRepository;
	
	@Autowired(required = false)
	public StorageService storageService;
	
	@Autowired
	Environment env;
	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Create new support")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Support create successfully  ", response = SupportVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createSupport(@ModelAttribute SupportRequest request) throws Exception {
		log.info("Start createSupport");
		super.validateRequest(request);
		Support support = supportService.create(request);
		if(support != null) {
			support = uploadFile(request, support);
		}
		return response(support.getVO());
		
	}	
	
	@ApiOperation(value = "Update support")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of support update", response = SupportVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updateSupport(
			@ApiParam(value = "Object json support") @ModelAttribute SupportRequest request) throws Exception {
		
		Integer id = request.getId();
		log.info("Update Support:" + request);
		Support existed = supportService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Support Id [" + id + "] invalid ");
		}
		
		Utils.copyNonNullProperties(request, existed);
		existed = supportService.save(existed);
		existed = uploadFile(request, existed);
		log.info("Patch Support with existed = " + existed);
		return response(existed.getVO());
	}
	
	private Support uploadFile(SupportRequest request, Support support) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			support = supportService.storePhoto(support,file);
		}
		return support;
	}	
	
	@ApiOperation(value = "Update support avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of support update", response = SupportVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity updateSupportPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json support") @RequestParam(value = "id", required = true) Integer id)
			throws Exception {
		log.info("Upload photo:" + id);
		Support existed = supportService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Support Id [" + id + "] invalid ");
		}

		if (file != null) {
			existed = supportService.storePhoto(existed,file);
		}
		existed = supportService.save(existed);
		log.info("Patch Support with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Get Support by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return support response ", response = SupportVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getSupport(
			@ApiParam(value = "Get support by Id", defaultValue = "1") @PathVariable Integer id) throws Exception {
		log.info("Get detail Support");

		Support support = supportService.findOne(id);
		log.info("Get support by getId [" + id + "]");
		if (support != null) {
			SupportVO supportVO = supportService.getDetail(support);
			return response(supportVO);
		} else {
			throw new NotFoundException("Support Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "List support by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = SupportVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listSupport(
			@RequestParam(required = false, defaultValue = "") String key,
			@RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "") String fromDate,
			@RequestParam(required = false, defaultValue = "") String toDate,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Support");
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		SupportRequest request = new SupportRequest(key, status, fromDate, toDate);
		Page<Support> result = supportRepository.search(request, paging);
		log.info("Search support total elements [" + result.getTotalElements() + "]");
		List<Support> contentList = result.getContent();
		List<SupportVO> responseList = new ArrayList<SupportVO>();
		
		contentList.forEach(support -> {
			responseList.add(supportService.getDetail(support));
		});
		
		return responseList(responseList, result);
	}

	@ApiOperation(value = "Delete support")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted support", response = Response.class),
			@ApiResponse(code = 201, message = "STARTED, Already registered also verified with Phone Number and Support name. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteSupport(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Support [" + id + "]");
		Support support = supportService.findOne(id);
		if (support != null) {
			log.info("Delete  Support [" + id + "]");
			supportService.delete(id);
			return responseMessage("Deleted  Support [" + id + "] sucessfully");
		} else {
			throw new NotFoundException("Support Id [" + id + "] invalid ");
		}
	}
	
}
