package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.UserGiftcode;
import vn.vme.io.game.UserGiftcodeRequest;


/**
 * @m-tech
 */
@Transactional
public interface UserGiftcodeRepository  extends PagingAndSortingRepository<UserGiftcode, Long> {
	
	@Query("SELECT u FROM UserGiftcode u WHERE 1=1 "
			+ " AND (cast(:#{#p.userId} as long) = 0 OR u.user.id= cast(:#{#p.userId} as long) )"
			+ " AND (cast(:#{#p.giftcodeId} as long) = 0 OR u.giftcode.id= cast(:#{#p.giftcodeId} as long) )"
			+ " AND (:#{#p.eventId} = 0 OR u.giftcode.event.id= :#{#p.eventId} )"
			+ " AND (:#{#p.gameId} = 0 OR u.giftcode.game.id= :#{#p.gameId} )"
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<UserGiftcode> search( @Param("p") UserGiftcodeRequest request,	Pageable paging);
	
	@Query("SELECT count(u.id) FROM UserGiftcode u WHERE 1=1 and u.giftcode.pool.id=?1")
	public int countByPoolId(int poolId);
	
	public UserGiftcode findByUserIdAndGiftcodeId(Long userId, Long giftcodeId);

	public UserGiftcode findByGiftcodeId(Long id);	
}
