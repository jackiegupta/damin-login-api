package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Banner;
import vn.vme.io.game.BannerRequest;


/**
 * @m-tech
 */
@Transactional
public interface BannerRepository  extends PagingAndSortingRepository<Banner, Integer> {
	
	@Query("SELECT u FROM Banner u WHERE 1=1 "
			+ " AND (:#{#p.name} is null OR length(:#{#p.name})=0 "
			+ "									OR UPPER(u.name) LIKE CONCAT('%',UPPER(:#{#p.name}),'%') )"
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<Banner> search( @Param("p") BannerRequest request,	Pageable paging);	
}
