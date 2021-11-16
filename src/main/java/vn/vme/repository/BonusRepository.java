package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Bonus;
import vn.vme.io.game.BonusRequest;


/**
 * @m-tech
 */
@Transactional
public interface BonusRepository  extends PagingAndSortingRepository<Bonus, Integer> {
	
	@Query("SELECT u FROM Bonus u WHERE 1=1 "
			+ " AND (:#{#p.name} is null OR length(:#{#p.name})=0 "
			+ "									OR LOWER(u.name) LIKE CONCAT('%',LOWER(:#{#p.name}),'%'))"
			+ " AND (:#{#p.levelId} is null OR :#{#p.levelId}=0 OR u.levelId = :#{#p.levelId})"
			+ " AND (:#{#p.scope} is null OR length(:#{#p.scope})=0 OR u.scope = :#{#p.scope}) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<Bonus> search( @Param("p") BonusRequest request,	Pageable paging);
	
}
