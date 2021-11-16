package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import vn.vme.entity.EventPool;

/**
 * @MTech
 */
@Transactional
public interface EventPoolRepository  extends PagingAndSortingRepository<EventPool, Integer> {

	void deleteByEventId(Integer eventId);
	
}
