package vn.vme.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import vn.vme.entity.User;

/**
 * @m-tech
 */
@Transactional
public interface RoleRepository  extends PagingAndSortingRepository<User, Long> {
	
	//List<String> getRoleByUserId(String userId);	 
}
