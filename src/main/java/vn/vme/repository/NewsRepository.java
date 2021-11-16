package vn.vme.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.News;
import vn.vme.io.game.NewsRequest;

/**
 * @MTech
 */
@Transactional
public interface NewsRepository  extends PagingAndSortingRepository<News, Integer> {
	
	@Query("SELECT g FROM News g WHERE g.status IN ('ACTIVE','COMING','LIVE','FINISHED')")
	public  Page<News> findByUserId(Long userId, Pageable paging);
	
	public List<News> findByStatus(String status);
	
	public Page<News> findByStatus(String status, Pageable paging);
	
	@Query("SELECT r FROM News r,CategoryNews s WHERE s.id = r.categoryNews.id "
			+ " AND (:#{#p.name} is null OR length(:#{#p.name})=0 OR LOWER(r.name) LIKE CONCAT('%',LOWER(:#{#p.name}),'%')) "
			+ " AND (:#{#p.categoryId} = 0 OR r.categoryNews.id = :#{#p.categoryId})"
			//+ " AND (:#{#p.fromDate} is null OR length(:#{#p.fromDate})=0 OR r.startDate >= TO_DATE(:#{#p.fromDate},'yyyy-mm-dd hh24:mi:ss')) "
			//+ " AND (:#{#p.toDate} is null OR length(:#{#p.toDate})=0 OR r.startDate <= TO_DATE(:#{#p.toDate},'yyyy-mm-dd hh24:mi:ss')) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR r.status = :#{#p.status}) ")
	public Page<News> search(@Param("p") NewsRequest request, Pageable paging);
	
}
