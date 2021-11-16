package vn.vme.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import vn.vme.entity.EventGame;

/**
 * @MTech
 */
@Transactional
public interface EventGameRepository  extends PagingAndSortingRepository<EventGame, Integer> {

	void deleteByEventId(Integer gameId);
	
}
