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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jayway.jsonpath.internal.Utils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.URI;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Payment;
import vn.vme.entity.Product;
import vn.vme.entity.User;
import vn.vme.exception.NotFoundException;
import vn.vme.io.finance.PaymentRequest;
import vn.vme.io.finance.PaymentVO;
import vn.vme.io.finance.VitapayTransactionRequest;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.PaymentRepository;
import vn.vme.service.PaymentService;
import vn.vme.service.ProductService;
import vn.vme.service.StorageService;
import vn.vme.service.UserService;

@RestController
@RequestMapping(URI.V1 + URI.PAYMENT)
public class PaymentController extends BaseController {

	static Logger log = LoggerFactory.getLogger(PaymentController.class.getName());
	@Autowired
	public PaymentService paymentService;
	
	@Autowired
	public PaymentRepository paymentRepository;
	
	@Autowired
	public StorageService storageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProductService productService;
	

	@ApiOperation(value = "Register new payment")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Payment successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Payment already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Payment already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = PaymentVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity registerPayment(@RequestBody PaymentRequest request)
			throws Exception {
		log.info("Payment registration: " + request);
		super.validateRequest(request);
		Payment payment = paymentService.create(request);
		return response(payment.getVO());
	}
	
	@ApiOperation(value = "Create new general payment")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Payment successfully registered ", response = PaymentVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Payment already Registered with Phone Number and Domain name but not yet verify", response = PaymentVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Payment already registered also verified. Go signin.", response = PaymentVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = PaymentVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, PaymentName is used, as in the response", response = PaymentVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = PaymentVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.VITAPAY, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity createPaymentVitaPay(@RequestBody VitapayTransactionRequest request)
			throws Exception {
		log.info("Creating payment: " + request);
		int productId = request.getProductId();
		
		UserVO userVO = UserContextHolder.getContext().getCurrentUser();
		
		User user = userService.findOne(userVO.getId());
		Product product = productService.findOne(productId);
		
		if (product == null) {
			throw new NotFoundException("Product Id [" + productId + "] invalid ");
		}
		if (user == null) {
			throw new NotFoundException("User invalid ");
		}
		
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setProductId(product.getId());
		paymentRequest.setUserId(user.getId());
		paymentRequest.setMethod(request.getMethodSelected());
		Payment payment = paymentService.create(paymentRequest);
		return response(payment.getVO());
	}
	
	@ApiOperation(value = "Create new general payment")
	@ApiResponses({
		@ApiResponse(code = 201, message = "CREATED, Payment successfully registered ", response = PaymentVO.class),
		@ApiResponse(code = 202, message = "ACCEPTED, Payment already Registered with Phone Number and Domain name but not yet verify", response = PaymentVO.class),
		@ApiResponse(code = 208, message = "ALREADY_REPORTED, Payment already registered also verified. Go signin.", response = PaymentVO.class),
		@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = PaymentVO.class),
		@ApiResponse(code = 409, message = "CONFLICT, PaymentName is used, as in the response", response = PaymentVO.class),
		@ApiResponse(code = 423, message = "LOCKED", response = PaymentVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity createPayment(@RequestBody PaymentRequest request)
			throws Exception {
		log.info("Creating payment: " + request);
		Payment payment = paymentService.create(request);
		return response(payment.getVO());
	}
	
	@ApiOperation(value = "Update payment")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of payment update", response = PaymentVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity updatePayment(@ApiParam(value = "Object json payment") @RequestBody PaymentRequest request)
			throws Exception {

		Long id = request.getId();
		log.info("Update Payment:" + request);
		Payment existed = paymentService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Payment Id [" + id + "] invalid ");
		}
		
		existed = paymentService.update(request, existed);
		log.info("Patch Payment with existed = " + existed);
		return response(existed.getVO());
	}
	
	@ApiOperation(value = "Get Payment by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return payment response  ", response = PaymentVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.OAUTH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity getVitapayToken(@ApiParam(value = "Get payment by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Long id) throws Exception {
		log.info("Get payment by id [" + id + "]");
		Payment payment = paymentService.findOne(id);
		if (payment != null) {
			return response(paymentService.load(payment.getVO()));
		} else {
			throw new NotFoundException("Payment Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "Get Payment by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return payment response  ", response = PaymentVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity getPayment(@ApiParam(value = "Get payment by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Long id) throws Exception {
		log.info("Get payment by id [" + id + "]");
		Payment payment = paymentService.findOne(id);
		if (payment != null) {
			return response(paymentService.load(payment.getVO()));
		} else {
			throw new NotFoundException("Payment Id [" + id + "] invalid ");
		}
	}
	
	@ApiOperation(value = "Get Payment by refId")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return payment response  ", response = PaymentVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.REFER, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity getPaymentByReferId(@ApiParam(value = "Get payment by refId", defaultValue = "1") 
	@RequestParam(required = false, defaultValue = "") String  refId) throws Exception {
		log.info("Get payment by refId [" + refId + "]");
		if(Utils.isEmpty(refId)) {
			throw new NotFoundException("Payment refId [" + refId + "] invalid ");
		}
		Payment payment = paymentService.findByRefId(refId);
		if (payment != null) {
			return response(paymentService.load(payment.getVO()));
		} else {
			throw new NotFoundException("Payment refId [" + refId + "] NotFound ");
		}
	}

	@ApiOperation(value = "Get Payment by user id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return payment response  ", response = PaymentVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity listPayment(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Integer productId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		UserVO user = UserContextHolder.getContext().getCurrentUser();
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		PaymentRequest request = new PaymentRequest(key, user.getId(), productId, status, "", "");
		Page<Payment> result = paymentRepository.search(request, paging);
		log.info("Search payment total elements [" + result.getTotalElements() + "]");
		List<Payment> contentList = result.getContent();
		List<PaymentVO> responseList = new ArrayList<PaymentVO>();

		for (Payment userPayment : contentList) {
			responseList.add(paymentService.load(userPayment.getVO()));
		}
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "List payment by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = PaymentVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'USER', 'GM', 'SUPPORT', 'ADMIN')")
	public ResponseEntity listPayment(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Long userId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Integer productId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Payment");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		PaymentRequest request = new PaymentRequest(key,userId, productId,status, "","");
		Page<Payment> result = paymentRepository.search(request, paging);
		log.info("Search payment total elements [" + result.getTotalElements() + "]");
		List<Payment> contentList = result.getContent();
		List<PaymentVO> responseList = new ArrayList<PaymentVO>();

		for (Payment payment : contentList) {
			responseList.add(paymentService.load(payment.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete payment")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted payment", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Payment fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deletePayment(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Long id) throws Exception {
		log.info("Delete  Payment [" + id + "]");
		Payment payment = paymentService.findOne(id);
		if (payment != null) {
			log.info("Delete  Payment [" + payment.getId() + "]");
			paymentService.delete(payment.getId());
			return responseMessage("Deleted  Payment [" + payment.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Payment id [" + id + "] invalid ");
		}
	}

	
}
