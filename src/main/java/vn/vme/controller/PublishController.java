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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.URI;
import vn.vme.entity.Publish;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.PublishRequest;
import vn.vme.io.game.PublishVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.PublishRepository;
import vn.vme.service.PublishService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.PUBLISH)
public class PublishController extends BaseController {

	static Logger log = LoggerFactory.getLogger(PublishController.class.getName());
	@Autowired
	public PublishService publishService;
	
	@Autowired
	public PublishRepository publishRepository;
	
	@Autowired
	public StorageService storageService;
	
	@ApiOperation(value = "Create new general publish")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Publish successfully registered ", response = PublishVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Publish already Registered with Phone Number and Domain name but not yet verify", response = PublishVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Publish already registered also verified. Go signin.", response = PublishVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = PublishVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, PublishName is used, as in the response", response = PublishVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = PublishVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity createPublish(@ModelAttribute PublishRequest request)
			throws Exception {
		log.info("Creating publish: " + request);
		Publish publish = publishService.create(request);
		if(publish != null) {
			publish = uploadFile(request, publish);
		}
		return response(publish.getVO());
	}
	
	private Publish uploadFile(PublishRequest request, Publish publish) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			publish = publishService.storePhoto(publish,file);
		}
		return publish;
	}

	@ApiOperation(value = "Update publish")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of publish update", response = PublishVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity updatePublish(@ApiParam(value = "Object json publish") @ModelAttribute PublishRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Publish:" + request);
		Publish existed = publishService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Publish Id [" + id + "] invalid ");
		}
		
		existed = publishService.update(request, existed);
		existed = uploadFile(request, existed);
		log.info("Patch Publish with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Update publish avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of publish update", response = PublishVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity updatePublishPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json publish") @RequestParam(value = "id", required = true) Integer id)
			throws Exception {
		log.info("Upload photo:" + id);
		Publish existed = publishService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Publish Id [" + id + "] invalid ");
		}

		if (file != null) {
			existed = publishService.storePhoto(existed,file);
		}
		existed = publishService.save(existed);
		log.info("Patch Publish with existed = " + existed);
		return response(existed.getVO());
	}

	@ApiOperation(value = "Get Publish by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return publish response  ", response = PublishVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getPublish(@ApiParam(value = "Get publish by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get publish by id [" + id + "]");
		Publish publish = publishService.findOne(id);
		if (publish != null) {
			return response(publishService.load(publish.getVO()));
		} else {
			throw new NotFoundException("Publish Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "List publish by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = PublishVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listPublish(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Publish");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		PublishRequest request = new PublishRequest(key, status);
		Page<Publish> result = publishRepository.search(request, paging);
		log.info("Search publish total elements [" + result.getTotalElements() + "]");
		List<Publish> contentList = result.getContent();
		List<PublishVO> responseList = new ArrayList<PublishVO>();

		for (Publish publish : contentList) {
			responseList.add(publishService.load(publish.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete publish")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted publish", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Publish fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity deletePublish(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Publish [" + id + "]");
		Publish publish = publishService.findOne(id);
		if (publish != null) {
			log.info("Delete  Publish [" + publish.getId() + "]");
			publishService.delete(publish.getId());
			return responseMessage("Deleted  Publish [" + publish.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Publish id [" + id + "] invalid ");
		}
	}

	
}
