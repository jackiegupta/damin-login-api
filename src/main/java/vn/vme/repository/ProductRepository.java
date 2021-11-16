package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Product;
import vn.vme.io.game.ProductRequest;


/**
 * @m-tech
 */
@Transactional
public interface ProductRepository  extends PagingAndSortingRepository<Product, Integer> {
	
	@Query("SELECT u FROM Product u WHERE 1=1 "
			//+ " AND (cast(:#{#p.amount}  as int) = 0 OR u.amount= cast(:#{#p.amount} as int) ) "
			//+ " AND (cast(:#{#p.price}  as int) = 0  OR u.price= cast(:#{#p.price}  as int) )"
			+ " AND (:#{#p.currency} is null OR length(:#{#p.currency})=0 OR u.currency = :#{#p.currency}) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<Product> search( @Param("p") ProductRequest request, Pageable paging);	
}
