package vn.vme.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Support;
import vn.vme.io.game.SupportRequest;

/**
 * @MTech
 */
@Transactional
public interface SupportRepository  extends PagingAndSortingRepository<Support, Integer> {
	
	public List<Support> findByStatus(String status);
	
	public Page<Support> findByStatus(String status, Pageable paging);
	
	@Query("SELECT r FROM Support r WHERE 1=1 "
			+ " AND (:#{#p.name} is null OR length(:#{#p.name})=0 OR LOWER(r.name) LIKE CONCAT('%',LOWER(:#{#p.name}),'%')) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR r.status = :#{#p.status}) ")
	public Page<Support> search(@Param("p") SupportRequest request, Pageable paging);
	
}
