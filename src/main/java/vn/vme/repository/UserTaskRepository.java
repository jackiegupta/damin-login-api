package vn.vme.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.UserTask;
import vn.vme.io.game.UserTaskRequest;


/**
 * @m-tech
 */
@Transactional
public interface UserTaskRepository  extends PagingAndSortingRepository<UserTask, Long> {
	
	@Query("SELECT u FROM UserTask u LEFT JOIN PoolTask po ON u.task.id = po.task.id WHERE 1=1 "
			+ " AND (cast(:#{#p.userId} as long) = 0 OR u.user.id= cast(:#{#p.userId} as long) )"
			+ " AND (:#{#p.poolId} = 0 OR po.pool.id = :#{#p.poolId})"
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<UserTask> search( @Param("p") UserTaskRequest request,	Pageable paging);	
	
	@Query("SELECT r FROM UserTask r WHERE 1=1 "
			+ " AND (:#{#p.updateDate} is null OR length(:#{#p.updateDate})=0 OR r.updateDate >= TO_DATE(:#{#p.updateDate},'dd-mm-yyyy hh24:mi:ss')) "
			+ " AND (cast(:#{#p.userId} as long) = 0 OR r.user.id= cast(:#{#p.userId} as long) )"
			+ " AND (:#{#p.taskId} = 0 OR r.task.id = :#{#p.taskId})"
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR r.status = :#{#p.status}) ")
	public List<UserTask> findUserTask(@Param("p") UserTaskRequest request);
}
