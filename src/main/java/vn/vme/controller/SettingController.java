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
import vn.vme.common.URI;
import vn.vme.entity.Setting;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.SettingRequest;
import vn.vme.io.game.SettingVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.SettingRepository;
import vn.vme.service.SettingService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.SETTING)
public class SettingController extends BaseController {

	static Logger log = LoggerFactory.getLogger(SettingController.class.getName());
	@Autowired
	public SettingService settingService;
	
	@Autowired
	public SettingRepository settingRepository;
	@Autowired
	public StorageService storageService;
	
	@ApiOperation(value = "Create new general setting")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Setting successfully registered ", response = SettingVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Setting already Registered with Phone Number and Domain name but not yet verify", response = SettingVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Setting already registered also verified. Go signin.", response = SettingVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = SettingVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, SettingName is used, as in the response", response = SettingVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = SettingVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity createSetting(@RequestBody SettingRequest request)
			throws Exception {
		log.info("Creating setting: " + request);
		Setting setting = settingService.create(request);
		return response(setting.getVO());
	}
	
	@ApiOperation(value = "Update setting")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of setting update", response = SettingVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity updateSetting(@ApiParam(value = "Object json setting") @RequestBody SettingRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Setting:" + request);
		Setting existed = settingService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Setting Id [" + id + "] invalid ");
		}
		
		existed = settingService.update(request, existed);
		log.info("Patch Setting with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Get Setting by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return setting response  ", response = SettingVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getSetting(@ApiParam(value = "Get setting by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get setting by id [" + id + "]");
		Setting setting = settingService.findOne(id);
		if (setting != null) {
			return response(settingService.load(setting.getVO()));
		} else {
			throw new NotFoundException("Setting Id [" + id + "] invalid ");
		}
	}
	@ApiOperation(value = "List setting by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = SettingVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listSetting(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Setting");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		SettingRequest request = new SettingRequest(key,status);
		Page<Setting> result = settingRepository.search(request, paging);
		log.info("Search setting total elements [" + result.getTotalElements() + "]");
		List<Setting> contentList = result.getContent();
		List<SettingVO> responseList = new ArrayList<SettingVO>();

		for (Setting setting : contentList) {
			responseList.add(settingService.load(setting.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete setting")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted setting", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Setting fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity deleteSetting(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Setting [" + id + "]");
		Setting setting = settingService.findOne(id);
		if (setting != null) {
			log.info("Delete  Setting [" + setting.getId() + "]");
			settingService.delete(setting.getId());
			return responseMessage("Deleted  Setting [" + setting.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Setting id [" + id + "] invalid ");
		}
	}

	
}
