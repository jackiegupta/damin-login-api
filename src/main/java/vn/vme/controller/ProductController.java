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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.URI;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Payment;
import vn.vme.entity.Product;
import vn.vme.exception.NotFoundException;
import vn.vme.io.finance.PaymentRequest;
import vn.vme.io.game.ProductRequest;
import vn.vme.io.game.ProductVO;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.PaymentRepository;
import vn.vme.repository.ProductRepository;
import vn.vme.service.ProductService;
import vn.vme.service.Rest360Service;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.PRODUCT)
public class ProductController extends BaseController {

	static Logger log = LoggerFactory.getLogger(ProductController.class.getName());
	@Autowired
	public ProductService productService;
	@Autowired
	public Rest360Service rest360Service;
	
	@Autowired
	public ProductRepository productRepository;
	
	@Autowired
	public PaymentRepository paymentRepository;
	
	@Autowired
	public StorageService storageService;

	@ApiOperation(value = "Register new product")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Product successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Product already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Product already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = ProductVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity registerProduct(@RequestBody ProductRequest request)
			throws Exception {
		log.info("Product registration: " + request);
		super.validateRequest(request);
		Product product = productService.create(request);
		return response(product.getVO());
	}
	
	@ApiOperation(value = "Create new general product")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Product successfully registered ", response = ProductVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Product already Registered with Phone Number and Domain name but not yet verify", response = ProductVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Product already registered also verified. Go signin.", response = ProductVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = ProductVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, ProductName is used, as in the response", response = ProductVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = ProductVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity createProduct(@RequestBody ProductRequest request)
			throws Exception {
		log.info("Creating product: " + request);
		Product product = productService.create(request);
		return response(product.getVO());
	}
	
	@ApiOperation(value = "Update product")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of product update", response = ProductVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ADMIN')")
	public ResponseEntity updateProduct(@ApiParam(value = "Object json product") @RequestBody ProductRequest request)
			throws Exception {

		Integer id = request.getId();
		log.info("Update Product:" + request);
		Product existed = productService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Product Id [" + id + "] invalid ");
		}
		
		existed = productService.update(request, existed);
		log.info("Patch Product with existed = " + existed);
		return response(existed.getVO());
	}

	@ApiOperation(value = "Get Product by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return product response  ", response = ProductVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getProduct(@ApiParam(value = "Get product by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Integer id) throws Exception {
		log.info("Get product by id [" + id + "]");
		Product product = productService.findOne(id);
		if (product != null) {
			return response(productService.load(product.getVO()));
		} else {
			throw new NotFoundException("Product Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "Get Product by user id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return product response  ", response = ProductVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listPayment(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Integer gameId,
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
		log.info("Search product total elements [" + result.getTotalElements() + "]");
		List<Payment> contentList = result.getContent();
		List<ProductVO> responseList = new ArrayList<ProductVO>();

		for (Payment payment : contentList) {
			responseList.add(productService.load(payment.getProduct().getVO()));
		}
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "List product by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = ProductVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listProduct(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Long userId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Integer price,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Integer amount,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String currency,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Product");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		ProductRequest request = new ProductRequest(userId, price, amount, currency, status);
		Page<Product> result = productRepository.search(request, paging);
		log.info("Search product total elements [" + result.getTotalElements() + "]");
		List<Product> contentList = result.getContent();
		List<ProductVO> responseList = new ArrayList<ProductVO>();

		for (Product product : contentList) {
			responseList.add(productService.load(product.getVO()));
		}
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "List product by conditions")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = ProductVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.PACKAGE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listProductPackage(
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search listProductPackage");
		Object result = rest360Service.listPackage();
		return response(result);
	}

	
	@ApiOperation(value = "Delete product")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted product", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Product fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteProduct(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Integer id) throws Exception {
		log.info("Delete  Product [" + id + "]");
		Product product = productService.findOne(id);
		if (product != null) {
			log.info("Delete  Product [" + product.getId() + "]");
			productService.delete(product.getId());
			return responseMessage("Deleted  Product [" + product.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Product id [" + id + "] invalid ");
		}
	}

	
}
