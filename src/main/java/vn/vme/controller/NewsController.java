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
import vn.vme.entity.News;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.NewsRequest;
import vn.vme.io.game.NewsVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.NewsRepository;
import vn.vme.service.NewsService;
import vn.vme.service.ResultService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.NEWS)
public class NewsController extends BaseController {

	static Logger log = LoggerFactory.getLogger(NewsController.class.getName());

	@Autowired
	public NewsService newsService;
	
	@Autowired
	public NewsRepository newsRepository;
	
	@Autowired
	public ResultService resultService;
	
	
	@Autowired(required = false)
	public StorageService storageService;
	
	@Autowired
	Environment env;
	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Create new news")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, News create successfully  ", response = NewsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createNews(@ModelAttribute NewsRequest request) throws Exception {
		log.info("Start createNews");
		super.validateRequest(request);
		News news = newsService.create(request);
		if(news != null) {
			news = uploadFile(request, news);
		}
		return response(news.getVO());
		
	}	
	
	@ApiOperation(value = "Update news")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of news update", response = NewsVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updateNews(
			@ApiParam(value = "Object json news") @ModelAttribute NewsRequest request) throws Exception {
		
		Integer id = request.getId();
		log.info("Update News:" + request);
		News existed = newsService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("News Id [" + id + "] invalid ");
		}
		
		Utils.copyNonNullProperties(request, existed);
		existed = newsService.save(existed);
		existed = uploadFile(request, existed);
		log.info("Patch News with existed = " + existed);
		return response(existed.getVO());
	}
	
	private News uploadFile(NewsRequest request, News news) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			news = newsService.storePhoto(news,file);
		}
		return news;
	}	
	
	@ApiOperation(value = "Update news avatar")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of news update", response = NewsVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.PHOTO, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity updateNewsPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
			@ApiParam(value = "Object json news") @RequestParam(value = "id", required = true) Integer id)
			throws Exception {
		log.info("Upload photo:" + id);
		News existed = newsService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("News Id [" + id + "] invalid ");
		}

		if (file != null) {
			existed = newsService.storePhoto(existed,file);
		}
		existed = newsService.save(existed);
		log.info("Patch News with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Get News by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return news response ", response = NewsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getNews(
			@ApiParam(value = "Get news by Id", defaultValue = "1") @PathVariable Integer id) throws Exception {
		log.info("Get detail News");

		News news = newsService.findOne(id);
		log.info("Get news by getId [" + id + "]");
		if (news != null) {
			NewsVO newsVO = newsService.getDetail(news);
			return response(newsVO);
		} else {
			throw new NotFoundException("News Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "List news by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = NewsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listNews(
			@RequestParam(required = false, defaultValue = "") String key,
			@RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "0") int categoryId,
			@RequestParam(required = false, defaultValue = "") String fromDate,
			@RequestParam(required = false, defaultValue = "") String toDate,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search News");
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		NewsRequest request = new NewsRequest(key, categoryId, status, fromDate, toDate);
		Page<News> result = newsRepository.search(request, paging);
		log.info("Search news total elements [" + result.getTotalElements() + "]");
		List<News> contentList = result.getContent();
		List<NewsVO> responseList = new ArrayList<NewsVO>();
		
		contentList.forEach(news -> {
			responseList.add(newsService.getDetail(news));
		});
		
		return responseList(responseList, result);
	}

	@ApiOperation(value = "Delete news")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted news", response = Response.class),
			@ApiResponse(code = 201, message = "STARTED, Already registered also verified with Phone Number and News name. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteNews(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  News [" + id + "]");
		News news = newsService.findOne(id);
		if (news != null) {
			log.info("Delete  News [" + id + "]");
			newsService.delete(id);
			return responseMessage("Deleted  News [" + id + "] sucessfully");
		} else {
			throw new NotFoundException("News Id [" + id + "] invalid ");
		}
	}
	
}
