package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import vn.vme.entity.Comment;

/**
 * @MTech
 */
@Transactional
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

	public Page<Comment> findByUserId(Long userId, Pageable paging);
	
}
