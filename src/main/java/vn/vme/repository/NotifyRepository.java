package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import vn.vme.entity.Notify;


/**
 * @m-tech
 */
@Transactional
public interface NotifyRepository  extends PagingAndSortingRepository<Notify, Long> {
	
	@Query("SELECT r FROM Notify r WHERE r.userId=?1 AND r.status='SUCCESS' ")
	public Page<Notify> findByUserId(Long userId, Pageable paging);
	
	@Query("SELECT r FROM Notify r WHERE (r.userId IS NULL or r.userId=0) AND r.status='SUCCESS' ")
	public Page<Notify> findCommonNotifications(Pageable paging);
}
