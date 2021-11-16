package vn.vme.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Partner;
import vn.vme.io.game.PartnerRequest;

/**
 * @MTech
 */
@Transactional
public interface PartnerRepository  extends PagingAndSortingRepository<Partner, Integer> {
	
	public List<Partner> findByStatus(String status);
	
	public Page<Partner> findByStatus(String status, Pageable paging);
	
	@Query("SELECT r FROM Partner r WHERE "
			+ " (:#{#p.name} is null OR length(:#{#p.name})=0 OR LOWER(r.name) LIKE CONCAT('%',LOWER(:#{#p.name}),'%')) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR r.status = :#{#p.status}) ")
	public Page<Partner> search(@Param("p") PartnerRequest request, Pageable paging);
	
}
