package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import vn.vme.entity.Email;

/**
 * @m-tech
 */
@Transactional
public interface EmailRepository  extends PagingAndSortingRepository<Email, Long> {
	 public Email findByReceiver(String receiver);
}
