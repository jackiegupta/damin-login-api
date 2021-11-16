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
import vn.vme.entity.Result;
import vn.vme.io.game.ResultRequest;
import vn.vme.io.game.ResultVO;
import vn.vme.repository.ResultRepository;

@Service
public class ResultService {
	private static final Logger log = LoggerFactory.getLogger(ResultService.class);

	@Autowired
	private ResultRepository resultRepository;

	//@Autowired(required=false)
    //MessagePublisher messagePublisher;

	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	RestTemplate restTemplate;
	public ResultService() {
	}
	
	public ResultVO load(ResultVO vo) {
		return vo;
	}
	public Result findOne(Long id) {
		return resultRepository.findById(id).get();
	}
	public Page<Result> search(ResultRequest request, Pageable pageable) {
		
		return resultRepository.search(request, pageable);
	}
	
	public Iterable<Result> findAll() {
		return resultRepository.findAll();
	}
	public Result create(ResultRequest request) {
		request.setStatus(Status.ACTIVE.name());
		Result result = new Result(request);
		result.setCreateDate(new Date());
		return this.save(result);
	}
	
	public Result update(ResultRequest request, Result existed) throws Exception {
		Result result = new Result(request);
		Utils.copyNonNullProperties(result, existed);
		return save(existed);
	}

	public Result save(Result result) {
		result = resultRepository.save(result);
		log.debug("save " + result);
		return result;
	}
	
	public void delete(Long id) {
		resultRepository.deleteById(id);
	}

	public Result findByUserIdAndTourId(Long userId, Integer tourId) {
		return resultRepository.findByUserIdAndTourId(userId, tourId);
	}

	public Integer countTour(Integer tourId) {
		return resultRepository.countTour(tourId);
	}
}
