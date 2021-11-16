package vn.vme.controller;

import java.util.ArrayList;
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
import vn.vme.common.Utils;
import vn.vme.entity.CategoryNews;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.CategoryNewsRequest;
import vn.vme.io.game.CategoryNewsVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.CategoryNewsRepository;
import vn.vme.service.CategoryNewsService;
import vn.vme.service.ResultService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.CATEGORY_NEWS)
public class CategoryNewsController extends BaseController {

	static Logger log = LoggerFactory.getLogger(CategoryNewsController.class.getName());

	@Autowired
	public CategoryNewsService categoryNewsService;
	
	@Autowired
	public CategoryNewsRepository categoryNewsRepository;
	
	@Autowired
	public ResultService resultService;
	
	
	@Autowired(required = false)
	public StorageService storageService;
	
	@Autowired
	Environment env;
	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Create new categoryNews")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, CategoryNews create successfully  ", response = CategoryNewsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createCategoryNews(@ModelAttribute CategoryNewsRequest request) throws Exception {
		log.info("Start createCategoryNews");
		super.validateRequest(request);
		CategoryNews category = categoryNewsService.create(request);
		if(category != null) {
			category = uploadFile(request, category);
		}
		return response(category.getVO());
		
	}

	private CategoryNews uploadFile(CategoryNewsRequest request, CategoryNews category) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			category = categoryNewsService.storePhoto(category,file);
		}
		return category;
	}	
	
	@ApiOperation(value = "Update categoryNews")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of categoryNews update", response = CategoryNewsVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updateCategoryNews(
			@ApiParam(value = "Object json categoryNews") @ModelAttribute CategoryNewsRequest request) throws Exception {
		
		Integer id = request.getId();
		log.info("Update CategoryNews:" + request);
		CategoryNews existed = categoryNewsService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("CategoryNews Id [" + id + "] invalid ");
		}
		Utils.copyNonNullProperties(request, existed);
		existed = categoryNewsService.save(existed);
		existed = uploadFile(request, existed);
		log.info("Patch CategoryNews with existed = " + existed);
		return response(existed.getVO());
	}
	
	
	@ApiOperation(value = "Update categoryNews avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of categoryNews update", response = CategoryNewsVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updateCategoryNewsPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json categoryNews") @RequestParam(value = "id", required = true) Integer id)
			throws Exception {
		log.info("Upload photo:" + id);
		CategoryNews existed = categoryNewsService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("CategoryNews Id [" + id + "] invalid ");
		}

		if (file != null) {
			existed = categoryNewsService.storePhoto(existed,file);
		}
		existed = categoryNewsService.save(existed);
		log.info("Patch CategoryNews with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Get CategoryNews by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return categoryNews response ", response = CategoryNewsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getCategoryNews(
			@ApiParam(value = "Get categoryNews by Id", defaultValue = "1") @PathVariable Integer id) throws Exception {
		log.info("Get detail CategoryNews");

		CategoryNews categoryNews = categoryNewsService.findOne(id);
		log.info("Get categoryNews by getId [" + id + "]");
		if (categoryNews != null) {
			return response(categoryNews.getVO());
		} else {
			throw new NotFoundException("CategoryNews Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "List categoryNews by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = CategoryNewsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listCategoryNews(
			@RequestParam(required = false, defaultValue = "") String key,
			@RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search CategoryNews");
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		CategoryNewsRequest request = new CategoryNewsRequest(key, status);
		Page<CategoryNews> result = categoryNewsRepository.search(request, paging);
		log.info("Search categoryNews total elements [" + result.getTotalElements() + "]");
		List<CategoryNews> contentList = result.getContent();
		List<CategoryNewsVO> responseList = new ArrayList<CategoryNewsVO>();
		
		contentList.forEach(categoryNews -> {
			responseList.add(categoryNews.getVO());
		});
		
		return responseList(responseList, result);
	}

	@ApiOperation(value = "Delete categoryNews")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted categoryNews", response = Response.class),
			@ApiResponse(code = 201, message = "STARTED, Already registered also verified with Phone Number and CategoryNews name. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteCategoryNews(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  CategoryNews [" + id + "]");
		CategoryNews categoryNews = categoryNewsService.findOne(id);
		if (categoryNews != null) {
			log.info("Delete  CategoryNews [" + id + "]");
			categoryNewsService.delete(id);
			return responseMessage("Deleted  CategoryNews [" + id + "] sucessfully");
		} else {
			throw new NotFoundException("CategoryNews Id [" + id + "] invalid ");
		}
	}
	
}
