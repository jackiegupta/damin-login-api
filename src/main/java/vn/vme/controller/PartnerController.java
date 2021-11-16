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
import vn.vme.entity.Partner;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.PartnerRequest;
import vn.vme.io.game.PartnerVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.PartnerRepository;
import vn.vme.service.PartnerService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.PARTNER)
public class PartnerController extends BaseController {

	static Logger log = LoggerFactory.getLogger(PartnerController.class.getName());

	@Autowired
	public PartnerService partnerService;
	
	@Autowired
	public PartnerRepository partnerRepository;
	
	@Autowired(required = false)
	public StorageService storageService;
	
	@Autowired
	Environment env;
	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Create new partner")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Partner create successfully  ", response = PartnerVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createPartner(@ModelAttribute PartnerRequest request) throws Exception {
		log.info("Start createPartner");
		super.validateRequest(request);
		Partner partner = partnerService.create(request);
		if(partner != null) {
			partner = uploadFile(request, partner);
		}
		return response(partner.getVO());
		
	}	
	
	@ApiOperation(value = "Update partner")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of partner update", response = PartnerVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updatePartner(
			@ApiParam(value = "Object json partner") @ModelAttribute PartnerRequest request) throws Exception {
		
		Integer id = request.getId();
		log.info("Update Partner:" + request);
		Partner existed = partnerService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Partner Id [" + id + "] invalid ");
		}
		
		Utils.copyNonNullProperties(request, existed);
		existed = partnerService.save(existed);
		existed = uploadFile(request, existed);
		log.info("Patch Partner with existed = " + existed);
		return response(existed.getVO());
	}
	
	private Partner uploadFile(PartnerRequest request, Partner partner) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			partner = partnerService.storePhoto(partner,file);
		}
		return partner;
	}	
	
	@ApiOperation(value = "Update partner avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of partner update", response = PartnerVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity updatePartnerPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json partner") @RequestParam(value = "id", required = true) Integer id)
			throws Exception {
		log.info("Upload photo:" + id);
		Partner existed = partnerService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Partner Id [" + id + "] invalid ");
		}

		if (file != null) {
			existed = partnerService.storePhoto(existed,file);
		}
		existed = partnerService.save(existed);
		log.info("Patch Partner with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Get Partner by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return partner response ", response = PartnerVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getPartner(
			@ApiParam(value = "Get partner by Id", defaultValue = "1") @PathVariable Integer id) throws Exception {
		log.info("Get detail Partner");

		Partner partner = partnerService.findOne(id);
		log.info("Get partner by getId [" + id + "]");
		if (partner != null) {
			PartnerVO partnerVO = partnerService.getDetail(partner);
			return response(partnerVO);
		} else {
			throw new NotFoundException("Partner Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "List partner by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = PartnerVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listPartner(
			@RequestParam(required = false, defaultValue = "") String key,
			@RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "") String fromDate,
			@RequestParam(required = false, defaultValue = "") String toDate,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Partner");
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		PartnerRequest request = new PartnerRequest(key, status, fromDate, toDate);
		Page<Partner> result = partnerRepository.search(request, paging);
		log.info("Search partner total elements [" + result.getTotalElements() + "]");
		List<Partner> contentList = result.getContent();
		List<PartnerVO> responseList = new ArrayList<PartnerVO>();
		
		contentList.forEach(partner -> {
			responseList.add(partnerService.getDetail(partner));
		});
		
		return responseList(responseList, result);
	}

	@ApiOperation(value = "Delete partner")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted partner", response = Response.class),
			@ApiResponse(code = 201, message = "STARTED, Already registered also verified with Phone Number and Partner name. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deletePartner(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Partner [" + id + "]");
		Partner partner = partnerService.findOne(id);
		if (partner != null) {
			log.info("Delete  Partner [" + id + "]");
			partnerService.delete(id);
			return responseMessage("Deleted  Partner [" + id + "] sucessfully");
		} else {
			throw new NotFoundException("Partner Id [" + id + "] invalid ");
		}
	}
	
}
