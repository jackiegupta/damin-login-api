package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Transaction;
import vn.vme.io.finance.TransactionRequest;


/**
 * @m-tech
 */
@Transactional
public interface TransactionRepository  extends PagingAndSortingRepository<Transaction, Long> {
	
	@Query("SELECT u FROM Transaction u WHERE 1=1 "
			+ " AND (cast(:#{#p.fromUserId} as long) = 0 OR u.fromUser.id= cast(:#{#p.fromUserId} as long) )"
			+ " AND (:#{#p.currency} is null OR length(:#{#p.currency})=0 OR u.currency = :#{#p.currency}) "
			+ " AND (:#{#p.typeChange} is null OR length(:#{#p.typeChange})=0 OR u.typeChange = :#{#p.typeChange}) "
			+ " AND (:#{#p.type} is null OR length(:#{#p.type})=0 OR u.type = :#{#p.type}) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<Transaction> search( @Param("p") TransactionRequest request, Pageable paging);	
}
