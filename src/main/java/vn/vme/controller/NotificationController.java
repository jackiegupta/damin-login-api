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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.URI;
import vn.vme.entity.Notify;
import vn.vme.io.notify.EmailVO;
import vn.vme.io.notify.NotificationVO;
import vn.vme.io.notify.NotificationRequest;
import vn.vme.io.notify.SmsVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.service.NotifyService;

@RestController
@RequestMapping(URI.V1 + URI.NOTIFICATION)
public class NotificationController extends BaseController {

	static Logger log = LoggerFactory.getLogger(NotificationController.class.getName());

	@Autowired
	public NotifyService notificationService;

	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Send new notification")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Notify sent successfully  ", response = SmsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity createNotify(@RequestBody NotificationRequest request) throws Exception {
		
		super.validateRequest(request);
		Notify notification =notificationService.sendMessageToDevice(request);
		return response(notification.getVO());
	}
	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Send new broadcast notification")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Notify sent successfully  ", response = SmsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(value = URI.ALL, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity createBroadcastNotify(@RequestBody NotificationRequest request) throws Exception {
		
		super.validateRequest(request);
		Notify notification = notificationService.sendMessageToAll(request);
		
		return response(notification.getVO());
	}
	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Send new broadcast notification")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Notify sent successfully  ", response = SmsVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@PostMapping(value = URI.LIST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity createBatchNotify(@RequestBody NotificationRequest request) throws Exception {
		
		super.validateRequest(request);
		List<Notify> notifications = notificationService.sendMessageToDevices(request);
		
		List<NotificationVO> responseList = new ArrayList<NotificationVO>();
		
		notifications.forEach(notification -> {
			responseList.add(notification.getVO());
		});
		
		return responseList(responseList);
	}
	
	@ApiOperation(value = "List notification by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = EmailVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listNotify(
			@RequestParam(required = false) Long userId, 
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "createDate") String sortProperty) throws Exception {
		log.info("Search notification");

		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		
		Page<Notify> result = null;
		if(userId != null && userId != 0) {
			result = notificationService.findByUser(userId, paging);
		}else {
			result = notificationService.findCommonNotifications(paging);
		}
		log.info("Search notification total elements [" + result.getTotalElements() + "]");
		List<Notify> contentList = result.getContent();
		List<NotificationVO> responseList = new ArrayList<NotificationVO>();
		
		contentList.forEach(notification -> {
			responseList.add(notification.getVO());
		});
		
		return responseList(responseList, result);
	}
	
	
}
