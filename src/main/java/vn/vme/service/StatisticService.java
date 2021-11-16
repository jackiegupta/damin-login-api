package vn.vme.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.vme.io.game.Statistic;
import vn.vme.io.game.StatisticVO;
import vn.vme.repository.DeviceRepository;
import vn.vme.repository.PaymentRepository;
import vn.vme.repository.UserRepository;


@Service
public class StatisticService {	 
	
	@Autowired
	DeviceRepository deviceRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PaymentRepository paymentRepository;

	static Logger log = LoggerFactory.getLogger(RoleService.class.getName());

	public List<Statistic> getCCUPCU(String startDate, String endDate) {
		return deviceRepository.statistic(startDate, endDate);
	}
	public List<Statistic> getCCUPCUDay(String startDate, String endDate) {
		return deviceRepository.statisticDay(startDate, endDate);
	}
	
	public List<Statistic> getNRU(String startDate, String endDate) {
		return userRepository.statisticNRU(startDate, endDate);
	}
	public List<Statistic> getNRUDay(String startDate, String endDate) {
		return userRepository.statisticNRUDay(startDate, endDate);
	}
	
	public List<Statistic> getDAU(String startDate, String endDate) {
		return userRepository.statisticDAU(startDate, endDate);
	}
	public List<Statistic> getDAUDay(String startDate, String endDate) {
		return userRepository.statisticDAUDay(startDate, endDate);
	}
	
	public List<Statistic> getDepositAccount(String startDate, String endDate) {
		return paymentRepository.statisticDepositAccount(startDate, endDate);
	}
	public List<Statistic> getDepositAccountDay(String startDate, String endDate) {
		return paymentRepository.statisticDepositAccountDay(startDate, endDate);
	}
	public List<Statistic> getDepositNewAccount(String startDate, String endDate) {
		return paymentRepository.statisticDepositNewAccount(startDate, endDate);
	}
	public List<Statistic> getDepositNewAccountDay(String startDate, String endDate) {
		return paymentRepository.statisticDepositNewAccountDay(startDate, endDate);
	}
	
	public List<Statistic> getARPUARPPU(String startDate, String endDate) {
		return paymentRepository.statisticARPUARPPU(startDate, endDate);
	}
	public List<Statistic> getARPUARPPUDay(String startDate, String endDate) {
		return paymentRepository.statisticARPUARPPUDay(startDate, endDate);
	}
	
	public List<Statistic> getRevenue(String startDate, String endDate) {
		return paymentRepository.statisticRevenue(startDate, endDate);
	}
	public List<Statistic> getRevenueDay(String startDate, String endDate) {
		return paymentRepository.statisticRevenueDay(startDate, endDate);
	}

	public StatisticVO load(StatisticVO vo) {
		return vo;
	}
}
