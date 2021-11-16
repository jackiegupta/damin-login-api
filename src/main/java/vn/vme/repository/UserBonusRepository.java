package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.UserBonus;
import vn.vme.io.game.UserBonusRequest;


/**
 * @m-tech
 */
@Transactional
public interface UserBonusRepository  extends PagingAndSortingRepository<UserBonus, Long> {
	
	@Query("SELECT u FROM UserBonus u WHERE 1=1 "
			+ " AND (cast(:#{#p.userId} as long) = 0 OR u.user.id= cast(:#{#p.userId} as long) )"
			+ " AND (:#{#p.bonusId} = 0 OR u.bonus.id = :#{#p.bonusId})")
	public Page<UserBonus> search( @Param("p") UserBonusRequest request,	Pageable paging);
	
	@Query("SELECT u FROM UserBonus u WHERE 1=1 "
			+ " AND (cast(:userId as long) = 0 OR u.user.id= cast(:userId as long) )"
			+ " AND (:bonusId = 0 OR u.bonus.id = :bonusId)")
	public UserBonus getBonus(Long userId, Integer bonusId);
	
}
