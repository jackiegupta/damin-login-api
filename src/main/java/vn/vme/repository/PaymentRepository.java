package vn.vme.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.vme.entity.Payment;
import vn.vme.io.finance.PaymentRequest;
import vn.vme.io.game.Statistic;


/**
 * @m-tech
 */
@Transactional
public interface PaymentRepository  extends PagingAndSortingRepository<Payment, Long> {
	
	@Query("SELECT u FROM Payment u WHERE 1=1 "
			+ " AND (cast(:#{#p.userId} as long) = 0 OR u.user.id= cast(:#{#p.userId} as long) )"
			+ " AND (:#{#p.status} is null OR length(:#{#p.status})=0 OR u.status = :#{#p.status}) ")
	public Page<Payment> search( @Param("p") PaymentRequest request, Pageable paging);

	public Payment findByRefId(String refId);
	
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.createDate , 'YYYY-MM-DD') as name, count(*) as value) from Payment d "
			+ " where 1=1 "
			+ " AND d.createDate > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.createDate < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.createDate , 'YYYY-MM-DD') "
			+ " order by to_char(d.createDate , 'YYYY-MM-DD') asc")
	public List<Statistic> statisticDepositAccount(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.createDate , 'YYYY-MM-DD HH24') as name, count(*) as value) from Payment d "
			+ " where 1=1 "
			+ " AND d.createDate > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.createDate < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.createDate , 'YYYY-MM-DD HH24') "
			+ " order by to_char(d.createDate , 'YYYY-MM-DD HH24') asc")
	public List<Statistic> statisticDepositAccountDay(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.createDate , 'YYYY-MM-DD') as name, count(*) as value) from Payment d "
			+ " where 1=1 "
			+ " AND d.createDate > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.createDate < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.createDate , 'YYYY-MM-DD'), d.user.id"
			+ " order by to_char(d.createDate , 'YYYY-MM-DD') asc")
	public List<Statistic> statisticDepositNewAccount(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.createDate , 'YYYY-MM-DD HH24') as name, count(*) as value) from Payment d "
			+ " where 1=1 "
			+ " AND d.createDate > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.createDate < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.createDate , 'YYYY-MM-DD HH24'), d.user.id"
			+ " order by to_char(d.createDate , 'YYYY-MM-DD HH24') asc")
	public List<Statistic> statisticDepositNewAccountDay(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.createDate , 'YYYY-MM-DD') as name, count(*) as value) from Payment d "
			+ " where 1=1 "
			+ " AND d.createDate > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.createDate < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.createDate , 'YYYY-MM-DD'), d.user.id"
			+ " order by to_char(d.createDate , 'YYYY-MM-DD') asc")
	public List<Statistic> statisticARPUARPPU(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.createDate , 'YYYY-MM-DD HH24') as name, count(*) as value) from Payment d "
			+ " where 1=1 "
			+ " AND d.createDate > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.createDate < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.createDate , 'YYYY-MM-DD HH24'), d.user.id"
			+ " order by to_char(d.createDate , 'YYYY-MM-DD HH24') asc")
	public List<Statistic> statisticARPUARPPUDay(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.createDate , 'YYYY-MM-DD') as name, SUM(d.price) as value) from Payment d "
			+ " where 1=1 "
			+ " AND d.createDate > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.createDate < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.createDate , 'YYYY-MM-DD')"
			+ " order by to_char(d.createDate , 'YYYY-MM-DD') asc")
	public List<Statistic> statisticRevenue(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.createDate , 'YYYY-MM-DD HH24') as name, SUM(d.price) as value) from Payment d "
			+ " where 1=1 "
			+ " AND d.createDate > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.createDate < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.createDate , 'YYYY-MM-DD HH24')"
			+ " order by to_char(d.createDate , 'YYYY-MM-DD HH24') asc")
	public List<Statistic> statisticRevenueDay(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
}
