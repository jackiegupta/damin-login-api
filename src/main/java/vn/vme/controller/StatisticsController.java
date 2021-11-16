package vn.vme.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.DateUtils;
import vn.vme.common.JConstants;
import vn.vme.common.URI;
import vn.vme.common.Utils;
import vn.vme.exception.AccessDeniedException;
import vn.vme.io.game.Statistic;
import vn.vme.io.game.StatisticVO;
import vn.vme.model.Response;
import vn.vme.service.StatisticService;

@RestController
@RequestMapping(URI.V1 + URI.STATISTICS)
public class StatisticsController extends BaseController {

	static Logger log = LoggerFactory.getLogger(StatisticsController.class.getName());
	@Autowired
	public StatisticService statisticsService;


	@ApiOperation(value = "List CCUPCU by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = StatisticVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.CCUPCU, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity CCUPCU(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String startDate,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String endDate)
			throws AccessDeniedException, Exception {
		log.info("Search CCUPCU");
		

		/*
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			if (authorities == null)
				throw new IllegalStateException("No user currently logged in");

			for (GrantedAuthority grantedAuthority : authorities) {
				log.debug(grantedAuthority.getAuthority());
			}
		}
		*/
		
		if (!startDate.contains("undefined--") && Utils.isNotEmpty(startDate) && DateUtils.toDateYYYYMMDD(startDate) != null) {
			startDate = startDate + " 00:00:00";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 00:00:00";
		}
		if (!endDate.contains("undefined--") && Utils.isNotEmpty(endDate) && DateUtils.toDateYYYYMMDD(endDate) != null) {
			endDate = endDate + " 23:59:59";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 23:59:59";
		}
		
		log.info("Search CCUPCU type:" + type);
		log.info("Search CCUPCU startDate:" + startDate);
		log.info("Search CCUPCU endDate:" + endDate);
		
		List<Statistic> contentList = null;
		if(type != null && type.toUpperCase().equals("D")) {
			contentList = statisticsService.getCCUPCUDay(startDate, endDate);
		}else {
			contentList = statisticsService.getCCUPCU(startDate, endDate);
		}
		
		return responseList(contentList);
	}
	
	@ApiOperation(value = "List NRU by conditions")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = StatisticVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.NRU, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity NRU(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String startDate,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String endDate)
					throws AccessDeniedException, Exception {
		log.info("Search NRU");
		
		if (!startDate.contains("undefined--") && Utils.isNotEmpty(startDate) && DateUtils.toDateYYYYMMDD(startDate) != null) {
			startDate = startDate + " 00:00:00";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 00:00:00";
		}
		if (!endDate.contains("undefined--") && Utils.isNotEmpty(endDate) && DateUtils.toDateYYYYMMDD(endDate) != null) {
			endDate = endDate + " 23:59:59";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 23:59:59";
		}
		
		log.info("Search NRU type:" + type);
		log.info("Search NRU startDate:" + startDate);
		log.info("Search NRU endDate:" + endDate);
		
		List<Statistic> contentList = null;
		if(type != null && type.toUpperCase().equals("D")) {
			contentList = statisticsService.getNRUDay(startDate, endDate);
		}else {
			contentList = statisticsService.getNRU(startDate, endDate);
		}
		
		return responseList(contentList);
	}
	
	@ApiOperation(value = "List DAU by conditions")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = StatisticVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.DAU, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity DAU(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String startDate,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String endDate)
					throws AccessDeniedException, Exception {
		log.info("Search DAU");
		
		if (!startDate.contains("undefined--") && Utils.isNotEmpty(startDate) && DateUtils.toDateYYYYMMDD(startDate) != null) {
			startDate = startDate + " 00:00:00";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 00:00:00";
		}
		if (!endDate.contains("undefined--") && Utils.isNotEmpty(endDate) && DateUtils.toDateYYYYMMDD(endDate) != null) {
			endDate = endDate + " 23:59:59";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 23:59:59";
		}
		
		log.info("Search DAU type:" + type);
		log.info("Search DAU startDate:" + startDate);
		log.info("Search DAU endDate:" + endDate);
		
		List<Statistic> contentList = null;
		if(type != null && type.toUpperCase().equals("D")) {
			contentList = statisticsService.getDAUDay(startDate, endDate);
		}else {
			contentList = statisticsService.getDAU(startDate, endDate);
		}
		
		return responseList(contentList);
	}
	
	
	@ApiOperation(value = "List DepositAccount by conditions")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = StatisticVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.DEPOSIT, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity DepositAccount(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String startDate,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String endDate)
					throws AccessDeniedException, Exception {
		log.info("Search DepositAccount");
		
		if (!startDate.contains("undefined--") && Utils.isNotEmpty(startDate) && DateUtils.toDateYYYYMMDD(startDate) != null) {
			startDate = startDate + " 00:00:00";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 00:00:00";
		}
		if (!endDate.contains("undefined--") && Utils.isNotEmpty(endDate) && DateUtils.toDateYYYYMMDD(endDate) != null) {
			endDate = endDate + " 23:59:59";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 23:59:59";
		}
		
		log.info("Search DepositAccount type:" + type);
		log.info("Search DepositAccount startDate:" + startDate);
		log.info("Search DepositAccount endDate:" + endDate);
		
		List<Statistic> contentList = null;
		if(type != null && type.toUpperCase().equals("D")) {
			contentList = statisticsService.getDepositAccountDay(startDate, endDate);
		}else {
			contentList = statisticsService.getDepositAccount(startDate, endDate);
		}
		
		return responseList(contentList);
	}
	
	@ApiOperation(value = "List DepositNewAccount by conditions")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = StatisticVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.DEPOSIT_NEW, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity DepositNewAccount(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String startDate,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String endDate)
					throws AccessDeniedException, Exception {
		log.info("Search DepositNewAccount");
		
		if (!startDate.contains("undefined--") && Utils.isNotEmpty(startDate) && DateUtils.toDateYYYYMMDD(startDate) != null) {
			startDate = startDate + " 00:00:00";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 00:00:00";
		}
		if (!endDate.contains("undefined--") && Utils.isNotEmpty(endDate) && DateUtils.toDateYYYYMMDD(endDate) != null) {
			endDate = endDate + " 23:59:59";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 23:59:59";
		}
		
		log.info("Search DepositNewAccount type:" + type);
		log.info("Search DepositNewAccount startDate:" + startDate);
		log.info("Search DepositNewAccount endDate:" + endDate);
		
		List<Statistic> contentList = null;
		if(type != null && type.toUpperCase().equals("D")) {
			contentList = statisticsService.getDepositNewAccountDay(startDate, endDate);
		}else {
			contentList = statisticsService.getDepositNewAccount(startDate, endDate);
		}
		
		return responseList(contentList);
	}
	
	
	@ApiOperation(value = "List ARPUARPPU by conditions")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = StatisticVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ARPU_ARPPU, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity ARPUARPPU(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String startDate,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String endDate)
					throws AccessDeniedException, Exception {
		log.info("Search ARPUARPPU");
		//ARPU tổng doanh thu/tổng số thành viên
		//ARPPU doanh thu trung bình trên mỗi người dùng trả
		if (!startDate.contains("undefined--") && Utils.isNotEmpty(startDate) && DateUtils.toDateYYYYMMDD(startDate) != null) {
			startDate = startDate + " 00:00:00";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 00:00:00";
		}
		if (!endDate.contains("undefined--") && Utils.isNotEmpty(endDate) && DateUtils.toDateYYYYMMDD(endDate) != null) {
			endDate = endDate + " 23:59:59";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 23:59:59";
		}
		
		log.info("Search ARPUARPPU type:" + type);
		log.info("Search ARPUARPPU startDate:" + startDate);
		log.info("Search ARPUARPPU endDate:" + endDate);
		
		List<Statistic> contentList = null;
		if(type != null && type.toUpperCase().equals("D")) {
			contentList = statisticsService.getARPUARPPUDay(startDate, endDate);
		}else {
			contentList = statisticsService.getARPUARPPU(startDate, endDate);
		}
		
		return responseList(contentList);
	}
	
	@ApiOperation(value = "List Revenue by conditions")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = StatisticVO.class),
		@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
		@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.REVENUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity Revenue(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String type,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String startDate,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String endDate)
					throws AccessDeniedException, Exception {
		log.info("Search Revenue");
		if (!startDate.contains("undefined--") && Utils.isNotEmpty(startDate) && DateUtils.toDateYYYYMMDD(startDate) != null) {
			startDate = startDate + " 00:00:00";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 00:00:00";
		}
		if (!endDate.contains("undefined--") && Utils.isNotEmpty(endDate) && DateUtils.toDateYYYYMMDD(endDate) != null) {
			endDate = endDate + " 23:59:59";
		}else {
			startDate = DateUtils.toDateString(JConstants.YYYY_MM_DD) + " 23:59:59";
		}
		
		log.info("Search Revenue type:" + type);
		log.info("Search Revenue startDate:" + startDate);
		log.info("Search Revenue endDate:" + endDate);
		
		List<Statistic> contentList = null;
		if(type != null && type.toUpperCase().equals("D")) {
			contentList = statisticsService.getRevenueDay(startDate, endDate);
		}else {
			contentList = statisticsService.getRevenue(startDate, endDate);
		}
		
		return responseList(contentList);
	}

}
