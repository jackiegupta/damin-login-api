package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.UserGame;
import vn.vme.io.game.UserGameRequest;


/**
 * @m-tech
 */
@Transactional
public interface UserGameRepository  extends PagingAndSortingRepository<UserGame, Long> {
	
	@Query("SELECT u FROM UserGame u WHERE 1=1 "
			+ " AND (cast(:#{#p.userId} as long) = 0 OR u.user.id= cast(:#{#p.userId} as long) )"
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<UserGame> search( @Param("p") UserGameRequest request,	Pageable paging);

	public UserGame findByUserIdAndGameId(Long userId, Integer gameId);	
}
