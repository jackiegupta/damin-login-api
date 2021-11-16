package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Vippoint;
import vn.vme.io.game.VippointRequest;


/**
 * @m-tech
 */
@Transactional
public interface VippointRepository  extends PagingAndSortingRepository<Vippoint, Integer> {
	
	 public Vippoint findByCode(String code);
	
	@Query("SELECT u FROM Vippoint u WHERE 1=1 "
			+ " AND (:#{#p.name} is null OR length(:#{#p.name})=0 "
			+ "									OR LOWER(u.name) LIKE CONCAT('%',LOWER(:#{#p.name}),'%') )"
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<Vippoint> search( @Param("p") VippointRequest request,	Pageable paging);	
}
