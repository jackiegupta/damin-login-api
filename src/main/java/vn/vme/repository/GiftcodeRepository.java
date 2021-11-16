package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Giftcode;
import vn.vme.io.game.GiftcodeRequest;

/**
 * @m-tech
 */
@Transactional
public interface GiftcodeRepository  extends PagingAndSortingRepository<Giftcode, Long> {
	
	@Query("SELECT u FROM Giftcode u WHERE 1=1 "
			+ " AND (:#{#p.code} is null OR length(:#{#p.code})=0 "
			+ "									OR LOWER(u.code) LIKE CONCAT('%',LOWER(:#{#p.code}),'%')) "
			+ " AND (cast(:#{#p.userId} as long) = 0 OR u.user.id= cast(:#{#p.userId} as long) )"
			+ " AND (:#{#p.gameId} is null OR :#{#p.gameId}=0 OR u.game.id = :#{#p.gameId}) "
			+ " AND (:#{#p.eventId} is null OR :#{#p.eventId}=0 OR u.event.id = :#{#p.eventId}) "
			+ " AND (:#{#p.poolId} is null OR :#{#p.poolId}=0 OR u.pool.id = :#{#p.poolId}) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<Giftcode> search( @Param("p") GiftcodeRequest request,	Pageable paging);
	
	@Query(value = "SELECT * FROM gate_schema.giftcode u WHERE u.pool_id = :poolId AND status ='ACTIVE' ORDER BY RANDOM() LIMIT 1 ", nativeQuery = true)
	public Giftcode findGiftcodeRandom(@Param("poolId") Integer poolId);	
	
	public Giftcode findByCode(String code);	
}
