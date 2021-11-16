package vn.vme.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Event;
import vn.vme.io.game.EventRequest;

/**
 * @MTech
 */
@Transactional
public interface EventRepository  extends PagingAndSortingRepository<Event, Integer> {
	
	@Query("SELECT g FROM Event g WHERE g.status IN ('ACTIVE','COMING','LIVE','FINISHED')")
	public  Page<Event> findByUserId(Long userId, Pageable paging);
	
	public List<Event> findByStatus(String status);
	
	public List<Event> findByStatusAndStartDateLessThan(String status, Date toDate);
	
	public Page<Event> findByStatusAndStartDateLessThan(String status, Date toDate, Pageable paging);
	
	@Query("SELECT g FROM Event g WHERE g.status IN ('ACTIVE','COMING','LIVE','FINISHED')")
	public List<Event> findEventInStatus();
	
	public Page<Event> findByStatus(String status, Pageable paging);
	
	@Query("SELECT r FROM Event r WHERE r.status IN ('LIVE','COMING')")
	public Page<Event> findSuggestEvent(Pageable paging);
	
	@Query("SELECT r FROM Event r WHERE 1=1 "
			+ " AND (:#{#p.name} is null OR length(:#{#p.name})=0 OR LOWER(r.name) LIKE CONCAT('%',LOWER(:#{#p.name}),'%')) "
			+ " AND (:#{#p.fromDate} is null OR length(:#{#p.fromDate})=0 OR r.startDate >= TO_DATE(:#{#p.fromDate},'yyyy-mm-dd hh24:mi:ss')) "
			+ " AND (:#{#p.toDate} is null OR length(:#{#p.toDate})=0 OR r.startDate <= TO_DATE(:#{#p.toDate},'yyyy-mm-dd hh24:mi:ss')) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR r.status = :#{#p.status}) ")
	public Page<Event> search(@Param("p") EventRequest request, Pageable paging);
	
}
