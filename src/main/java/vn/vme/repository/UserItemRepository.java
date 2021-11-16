package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.UserItem;
import vn.vme.io.game.UserItemRequest;


/**
 * @m-tech
 */
@Transactional
public interface UserItemRepository  extends PagingAndSortingRepository<UserItem, Long> {
	
	@Query("SELECT u FROM UserItem u WHERE 1=1 "
			+ " AND (cast(:#{#p.userId} as long) = 0 OR u.user.id= cast(:#{#p.userId} as long) )"
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<UserItem> search( @Param("p") UserItemRequest request,	Pageable paging);	
}
