package vn.vme.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Comment;
import vn.vme.exception.NotFoundException;
import vn.vme.io.notify.CommentRequest;
import vn.vme.io.notify.CommentVO;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.service.CommentService;

@RestController
@RequestMapping(URI.V1 + URI.COMMENT)
public class CommentController extends BaseController {

	static Logger log = LoggerFactory.getLogger(CommentController.class.getName());

	@Autowired
	public CommentService commentService;

	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Send new comment")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Comment sent successfully  ", response = CommentVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createComment(@RequestBody CommentRequest request) throws Exception {
		
		super.validateRequest(request);
		Comment comment = commentService.create(request);
		return response(comment.getVO());
	}	
	
	@ApiOperation(value = "Update comment")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of comment update", response = CommentVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateComment(
			@ApiParam(value = "Object json comment") @RequestBody CommentRequest request) throws Exception {
		
		Long id = request.getId();
		log.info("Update Comment:" + request);
		Comment existed = commentService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Comment Id [" + id + "] invalid ");
		}
		
		Utils.copyNonNullProperties(request, existed);
		existed = commentService.save(existed);
		log.info("Patch Comment with existed = " + existed);
		return response(existed.getVO());
	}

	@ApiOperation(value = "Get Comment by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return comment response ", response = CommentVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getComment(
			@ApiParam(value = "Get comment by Id", defaultValue = "1") @PathVariable Long id) throws Exception {
		log.info("Search Comment");

		Comment comment = commentService.findOne(id);
		log.info("Get comment by getId [" + id + "]");
		if (comment != null) {
			return response(comment.getVO());
		} else {
			throw new NotFoundException("Comment Id [" + id + "] invalid ");
		}
	}
	@ApiOperation(value = "List comment by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = CommentVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listComment(
			@RequestParam(required = false) Long userId, 
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "createDate") String sortProperty) throws Exception {
		log.info("Search Comment");

		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		Page<Comment> result = commentService.findAll(paging);
		log.info("Search comment total elements [" + result.getTotalElements() + "]");
		List<Comment> contentList = result.getContent();
		List<CommentVO> responseList = new ArrayList<CommentVO>();
		
		contentList.forEach(comment -> {
			responseList.add(commentService.getDetail(comment));
		});
		
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "List all comment of room by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = CommentVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.USER + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listRoomComment(
			@PathVariable(name = "id", required = true) Long roomId, 
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "createDate") String sortProperty) throws Exception {
		log.info("List comment of room");

		Order[] orders = new Order[1];
		orders[0] = new Order(Direction.DESC, "createDate");
		Pageable paging = new Paging().getPageRequest(page, size, orders);
		Page<Comment> result = commentService.findByUserId(roomId, paging);
		log.info("Search comment total elements [" + result.getTotalElements() + "]");
		List<CommentVO> responseList = new ArrayList<CommentVO>();
		List<Comment> contentList = result.getContent();
		
		contentList.forEach(comment -> {
			CommentVO commentVO = commentService.getDetail(comment);
			responseList.add(commentVO);
		});
		
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "Delete comment")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted comment", response = Response.class),
			@ApiResponse(code = 201, message = "STARTED, Already registered also verified with Phone Number and Comment name. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteComment(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Long id) throws Exception {
		log.info("Delete  Comment [" + id + "]");
		Comment comment = commentService.findOne(id);
		if (comment != null) {
			log.info("Delete  Comment [" + id + "]");
			commentService.delete(id);
			return response("Deleted  Comment [" + id + "] sucessfully");
		} else {
			throw new NotFoundException("Comment Id [" + id + "] invalid ");
		}
	}
}
