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
import vn.vme.entity.Product;
import vn.vme.io.game.ProductRequest;
import vn.vme.io.game.ProductVO;
import vn.vme.repository.ProductRepository;

@Service
public class ProductService {
	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductRepository productRepository;
	
	//@Autowired(required=false)
    //MessagePublisher messagePublisher;

	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RestTemplate restTemplate;
	public ProductService() {
	}
	
	public ProductVO load(ProductVO vo) {
		return vo;
	}
	public Product findOne(Integer id) {
		return productRepository.findById(id).get();
	}
	public Page<Product> search(ProductRequest request, Pageable pageable) {
		
		return productRepository.search(request, pageable);
	}
	
	public Iterable<Product> findAll() {
		return productRepository.findAll();
	}
	public Product create(ProductRequest request) {
		request.setStatus(Status.INACTIVE.name());
		Product product = new Product(request);
		product.setCreateDate(new Date());
		this.save(product);
		return product;
	}
	
	public Product update(ProductRequest request, Product existed) throws Exception {
		log.debug("update " + request);
		Product product = new Product(request);
		Utils.copyNonNullProperties(product, existed);
		existed = save(existed);
		return existed;
	}

	public Product save(Product product) {
		Integer id = product.getId();
		product = productRepository.save(product);
		log.debug("save " + product);
		return product;
	}
	
	public void delete(Integer id) {
		productRepository.deleteById(id);
	}
}
