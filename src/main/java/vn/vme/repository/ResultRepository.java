package vn.vme.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Result;
import vn.vme.io.game.ResultRequest;

/**
 * @MTech
 */
@Transactional
public interface ResultRepository extends PagingAndSortingRepository<Result, Long> {
	
	public List<Result> findByTourId(Long tourId);
	
	@Query("SELECT u FROM Result u WHERE 1=1 "
			//+ " AND (:#{#p.position} >0	OR u.position = :#{#p.position} ) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<Result> search( @Param("p") ResultRequest request, Pageable paging);

	public Result findByUserIdAndTourId(Long userId, Integer tourId);
	
	@Query("SELECT COUNT(*) FROM Result u WHERE u.tour.id = :tourId")
	public Integer countTour(@Param("tourId") Integer tourId);
}
