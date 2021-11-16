package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Game;
import vn.vme.io.game.GameRequest;


/**
 * @m-tech
 */
@Transactional
public interface GameRepository  extends PagingAndSortingRepository<Game, Integer> {
	
	@Query("SELECT u FROM Game u WHERE 1=1 "
			+ " AND (:#{#p.name} is null OR length(:#{#p.name})=0 "
			+ "									OR LOWER(u.name) LIKE CONCAT('%',LOWER(:#{#p.name}),'%'))"
			//+ "									OR LOWER(u.content) LIKE CONCAT('%',:#{#p.name},'%') "
			//+ "									OR LOWER(u.linkGame) LIKE CONCAT('%',:#{#p.name},'%'))"
			+ " AND (:#{#p.gameTypeId} is null OR :#{#p.gameTypeId}=0 OR u.gameType.id = :#{#p.gameTypeId}) "
			+ " AND (:#{#p.publishId} is null OR :#{#p.publishId}=0 OR u.publish.id = :#{#p.publishId}) "
			+ " AND (:#{#p.news} is null OR u.news = :#{#p.news}) "
			+ " AND (:#{#p.hot} is null OR u.hot = :#{#p.hot}) "
			+ " AND (:#{#p.top} is null OR u.top = :#{#p.top}) "
			+ " AND (:#{#p.h5sdk} is null OR u.h5sdk = :#{#p.h5sdk}) "
			+ " AND (:#{#p.platform} is null OR length(:#{#p.platform})=0 OR u.platform = :#{#p.platform}) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<Game> search( @Param("p") GameRequest request,	Pageable paging);	
}
