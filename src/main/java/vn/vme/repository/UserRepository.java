package vn.vme.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import vn.vme.entity.User;
import vn.vme.io.game.Statistic;
import vn.vme.io.user.UserRequest;

@Transactional
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	public User findByUserName(String userName);

	public User findByReferCode(String referCode);

	public User findByEmail(String email);

	public User findByPhone(String phone);

	public User findByUserNameAndEmailAndPhone(String userName, String email, String phone);

	public User findByUserNameOrEmailOrPhone(String userName, String email, String phone);

	User findByProviderAndExternalId(String provider, String externalId);

	User findByEmailOrProviderAndExternalId(String emai, String provider, String externalId);

	@Query("SELECT u FROM User u WHERE 1=1 AND (id<100 OR ID >1000) "
			+ " AND (:#{#p.userName} is null OR length(:#{#p.userName})=0 "
			+ "									OR LOWER(u.userName) LIKE CONCAT('%',LOWER(:#{#p.userName}),'%') "
			+ "									OR u.phone LIKE CONCAT('%',:#{#p.userName},'%') "
			+ "									OR LOWER(u.fullName) LIKE CONCAT('%',LOWER(:#{#p.userName}),'%') "
			+ "									OR LOWER(u.email) LIKE CONCAT('%',LOWER(:#{#p.userName}),'%')) "
			+ " AND (:#{#p.level} is null OR length(:#{#p.level})=0 OR u.level = :#{#p.level}) "
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<User> search(@Param("p") UserRequest request, Pageable paging);

	// Count user register in currentDate
	@Query("SELECT COUNT(*) FROM User u WHERE u.createDate = :createDate")
	public long countByLoginTimeGreaterThan(@Param("createDate") Date createDate);
	
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.createDate , 'YYYY-MM-DD') as name, count(*) as value) from User d "
			+ " where 1=1 "
			+ " AND d.createDate > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.createDate < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.createDate , 'YYYY-MM-DD') "
			+ " order by to_char(d.createDate , 'YYYY-MM-DD') asc")
	public List<Statistic> statisticNRU(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.createDate , 'YYYY-MM-DD HH24') as name, count(*) as value) from User d "
			+ " where 1=1 "
			+ " AND d.createDate > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.createDate < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.createDate , 'YYYY-MM-DD HH24') "
			+ " order by to_char(d.createDate , 'YYYY-MM-DD HH24') asc")
	public List<Statistic> statisticNRUDay(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.lastLogin , 'YYYY-MM-DD') as name, count(*) as value) from User d "
			+ " where 1=1 "
			+ " AND d.lastLogin > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.lastLogin < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.lastLogin , 'YYYY-MM-DD') "
			+ " order by to_char(d.lastLogin , 'YYYY-MM-DD') asc")
	public List<Statistic> statisticDAU(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.lastLogin , 'YYYY-MM-DD HH24') as name, count(*) as value) from User d "
			+ " where 1=1 "
			+ " AND d.lastLogin > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.lastLogin < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.lastLogin , 'YYYY-MM-DD HH24') "
			+ " order by to_char(d.lastLogin , 'YYYY-MM-DD HH24') asc")
	public List<Statistic> statisticDAUDay(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
