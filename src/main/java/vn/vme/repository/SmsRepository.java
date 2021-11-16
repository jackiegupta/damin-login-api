package vn.vme.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import vn.vme.entity.Sms;

/**
 * @m-tech
 */
@Transactional
public interface SmsRepository  extends PagingAndSortingRepository<Sms, Long> {
	 public Sms findByPhone(String phone);
	 //@Query("SELECT count(id) FROM Sms s WHERE s.create_date > CURRENT_TIMESTAMP - interval '30 minutes'")
	 public Integer countByCreateDateGreaterThan(Date createDate);
}
