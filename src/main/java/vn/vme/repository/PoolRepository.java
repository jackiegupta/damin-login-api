package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Game;
import vn.vme.entity.Pool;
import vn.vme.io.game.PoolRequest;


/**
 * @m-tech
 */
@Transactional
public interface PoolRepository  extends PagingAndSortingRepository<Pool, Integer> {
	
	@Query("SELECT u FROM Pool u WHERE 1=1 "
			+ " AND (:#{#p.scope} is null OR length(:#{#p.scope})=0 OR u.scope = :#{#p.scope}) "
			+ " AND (:#{#p.type} is null OR length(:#{#p.type})=0 OR u.type = :#{#p.type}) "
			//+ " AND (:#{#p.gameId} = 0 OR u.game.id = :#{#p.gameId})"
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<Pool> search( @Param("p") PoolRequest request,	Pageable paging);
	
	/*
	@Query("SELECT s FROM Game s INNER JOIN Pool u ON (u.game.id = s.id)  WHERE 1 = 1 "
			+ " AND (:#{#p.gameName} is null OR length(:#{#p.gameName})=0 OR UPPER(s.name) LIKE CONCAT('%',UPPER(:#{#p.gameName}),'%')) "	
			+ " AND (:#{#p.scope} is null OR length(:#{#p.scope})=0 OR u.scope = :#{#p.scope}) "
			+ " AND (:#{#p.type} is null OR length(:#{#p.type})=0 OR u.type = :#{#p.type}) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) group by s.id ")
	public Page<Game> searchByGame( @Param("p") PoolRequest request,	Pageable paging);*/	
}
