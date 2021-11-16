package vn.vme.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.vme.common.JConstants.Status;
import vn.vme.common.Utils;
import vn.vme.entity.Payment;
import vn.vme.entity.Product;
import vn.vme.entity.User;
import vn.vme.io.finance.PaymentRequest;
import vn.vme.io.finance.PaymentVO;
import vn.vme.repository.PaymentRepository;

@Service
public class PaymentService {
	private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	private PaymentRepository paymentRepository;
	
	//@Autowired(required=false)
    //MessagePublisher messagePublisher;

	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	RestTemplate restTemplate;
	public PaymentService() {
	}
	
	public PaymentVO load(PaymentVO vo) {
		return vo;
	}
	public Payment findOne(Long id) {
		return paymentRepository.findById(id).get();
	}
	public Page<Payment> search(PaymentRequest request, Pageable pageable) {
		
		return paymentRepository.search(request, pageable);
	}
	
	public Iterable<Payment> findAll() {
		return paymentRepository.findAll();
	}
	public Payment create(PaymentRequest request) {
		request.setStatus(Status.INACTIVE.name());
		User user = userService.findOne(request.getUserId());
		Product product = productService.findOne(request.getProductId());
		Payment payment = new Payment(request);
		payment.setUser(user);
		payment.setProduct(product);
		payment.setMethod(request.getMethod());
		int price = product.getPrice() - product.getPrice()*product.getDiscount()/100;
		payment.setPrice(price);
		payment.setBillId(Utils.genTid());
		payment.setRefId(Utils.genRefId());
		payment.setCreateDate(new Date());
		return this.save(payment);
	}
	
	public Payment update(PaymentRequest request, Payment existed) throws Exception {
		log.debug("update " + request);
		Payment payment = new Payment(request);
		Utils.copyNonNullProperties(payment, existed);
		return save(existed);
	}

	public Payment save(Payment payment) {
		log.debug("save " + payment);
		Long id = payment.getId();
		payment = paymentRepository.save(payment);
		log.debug("save " + id);
		return payment;
	}
	
	public void delete(Long id) {
		paymentRepository.deleteById(id);
	}

	public Payment findByRefId(String refId) {
		return paymentRepository.findByRefId(refId);
	}
}
