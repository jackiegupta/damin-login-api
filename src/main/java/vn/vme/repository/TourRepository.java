package vn.vme.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Tour;
import vn.vme.io.game.TourRequest;

/**
 * @MTech
 */
@Transactional
public interface TourRepository  extends PagingAndSortingRepository<Tour, Integer> {
	
	@Query("SELECT g FROM Tour g WHERE g.status IN ('ACTIVE','COMING','LIVE','FINISHED')")
	public  Page<Tour> findByUserId(Long userId, Pageable paging);
	
	public List<Tour> findByStatus(String status);
	
	public List<Tour> findByStatusAndStartDateLessThan(String status, Date toDate);
	
	public Page<Tour> findByStatusAndStartDateLessThan(String status, Date toDate, Pageable paging);
	
	@Query("SELECT g FROM Tour g WHERE g.status IN ('ACTIVE','COMING','LIVE','FINISHED')")
	public List<Tour> findTourInStatus();
	
	public Page<Tour> findByStatus(String status, Pageable paging);
	
	@Query("SELECT r FROM Tour r WHERE r.status IN ('LIVE','COMING')")
	public Page<Tour> findSuggestTour(Pageable paging);
	
	@Query("SELECT r FROM Tour r WHERE r.status IN ('ACTIVE','LIVE','COMING') and now() >= r.endDate")
	public Page<Tour> findFinishTour(Pageable paging);
	
	@Query("SELECT r FROM Tour r,Game s WHERE s.id = r.game.id "
			+ " AND (:#{#p.name} is null OR length(:#{#p.name})=0 OR LOWER(r.name) LIKE CONCAT('%',LOWER(:#{#p.name}),'%')) "			
			+ " AND (cast(:#{#p.fromPrice} as long) =0 OR r.price >= cast(:#{#p.fromPrice} as long) ) "
			+ " AND (cast(:#{#p.toPrice} as long) =0 OR r.price <= cast(:#{#p.toPrice} as long) ) "
			+ " AND (:#{#p.fromDate} is null OR length(:#{#p.fromDate})=0 OR r.startDate >= TO_DATE(:#{#p.fromDate},'yyyy-mm-dd hh24:mi:ss')) "
			+ " AND (:#{#p.toDate} is null OR length(:#{#p.toDate})=0 OR r.startDate <= TO_DATE(:#{#p.toDate},'yyyy-mm-dd hh24:mi:ss')) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR r.status = :#{#p.status}) ")
	public Page<Tour> search(@Param("p") TourRequest request, Pageable paging);
	
}
