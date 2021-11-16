package vn.vme.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import vn.vme.entity.Device;
import vn.vme.io.game.Statistic;
import vn.vme.io.user.DeviceVO;

@Transactional
public interface DeviceRepository extends PagingAndSortingRepository<Device, Long> {

	public Device findByDeviceId(String deviceId);

	@Query("SELECT u FROM Device u WHERE 1=1")
	public Page<Device> search(@Param("p") DeviceVO request, Pageable paging);

	public long countByLoginTimeGreaterThan(Date loginTime);
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.loginTime , 'YYYY-MM-DD') as name, count(*) as value) from Device d "
			+ " where 1=1 "
			+ " AND d.loginTime > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.loginTime < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.loginTime , 'YYYY-MM-DD') "
			+ " order by to_char(d.loginTime , 'YYYY-MM-DD') asc")
	public List<Statistic> statistic(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	@Query("select new vn.vme.io.game.Statistic(to_char(d.loginTime , 'YYYY-MM-DD HH24') as name, count(*) as value) from Device d "
			+ " where 1=1 "
			+ " AND d.loginTime > to_date(:startDate,'YYYY-MM-DD HH24:MI:SS') AND d.loginTime < to_date(:endDate,'YYYY-MM-DD HH24:MI:SS')"
			+ " group by to_char(d.loginTime , 'YYYY-MM-DD HH24') "
			+ " order by to_char(d.loginTime , 'YYYY-MM-DD HH24') asc")
	public List<Statistic> statisticDay(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
