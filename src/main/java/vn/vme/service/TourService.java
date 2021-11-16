package vn.vme.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.guardduty.model.Member;

import vn.vme.common.JConstants.TourStatus;
import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Game;
import vn.vme.entity.Tour;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.TourRequest;
import vn.vme.io.game.TourVO;
import vn.vme.repository.TourRepository;

@Service
public class TourService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(TourService.class);

	@Autowired
	private TourRepository tourRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private StorageService storageService;
	
	//MessagePublisher messagePublisher;

	
	@Autowired
	Environment env;

	@Value("${imageUrl}")
	private String imageUrl;
	
	public TourService() {
		log.debug("TourService");
	}

	public TourVO load(TourVO vo) {
		return vo;
	}
	
	public Tour findOne(Integer id) {
		return tourRepository.findById(id).orElse(null);
	}

	public Page<Tour> findByUserId(Long userId, Pageable paging) {
		return tourRepository.findByUserId(userId, paging);
	}

	public Page<Tour> findAll(Pageable pageable) {
		return tourRepository.findAll(pageable);
	}

	public Tour save(Tour tour) {
		Integer id = tour.getId();
		boolean isNew = id == null || id == 0;
		tour = tourRepository.save(tour);
		return tour;
	}

	public void delete(Integer id) {
		tourRepository.deleteById(id);

	}

	
	public Tour create(TourRequest request) {
		request.setStatus(TourStatus.ACTIVE.name());
		Game game = gameService.findOne(request.getGameId());
		Long adminId = UserContextHolder.getContext().getCurrentUser().getId();

		Tour tour = new Tour(request);
		tour.setGame(game);
		tour.setCreateId(adminId);
		tour.setStartDate(tour.getStartDate());
		tour.setCreateDate(new Date());
		Set<Member> userList = new HashSet<Member>(1);
		//userList.add(new Member(adminId, adminId));
		tour = this.save(tour);
		return tour;
	}

	public void startTour(Tour tour) {
		tour.setStatus(TourStatus.COMING.name());
		this.save(tour);
		//messagePublisher.publishTourChange(JConstants.CREATE_EVENT, tour.getVO());
	}

	public List<Tour> findByStatus(String status) {
		return tourRepository.findByStatus(status);
	}

	public Page<Tour> findSuggestTour(Pageable paging) {
		return tourRepository.findSuggestTour(paging);
	}

	public Page<Tour> findByStatusAndStartDateLessThan(String status, Date next2Days, Pageable paging) {
		return tourRepository.findByStatusAndStartDateLessThan(status, next2Days, paging);
	}

	public List<Tour> findByStatusAndStartDateLessThan(String status, Date next2Days) {
		return tourRepository.findByStatusAndStartDateLessThan(status, next2Days);
	}

	public Page<Tour> listHomeTour(String status, Pageable paging) {
		return tourRepository.findByStatus(status, paging);
	}

	public TourVO getDetail(Integer id) {
		return getDetail(findOne(id));
	}

	public TourVO getDetail(Tour tour) {
		TourVO tourVO = tour.getVO();
		if (tour.getWinUserId() != null) {
			tourVO.setWinUser(userService.findOne(tour.getWinUserId()).getVO().getDO());
		} 
		tourVO.setGame(gameService.load(tourVO.getGame()));
		return tourVO;
	}
	public Tour storePhoto(Tour existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = Tour.class.getSimpleName().toLowerCase();
			storageService.store(file, fileName, folder);
			existed.setPhoto(imageUrl + folder + File.separator + fileName);


			if (Utils.isNotEmpty(oldFile) && !Utils.isTestPhoto(oldFile)) {
				storageService.deleteFile(oldFile);
			}
		} catch (Exception ex) {
			log.warn("failed to delete file:{}", ex);
		}
		return save(existed);
	}
	public void changeStatus(Tour tour, TourStatus status) {
		tour.setStatus(status.name());
		String currentStatus = tour.getStatus();
		TourVO tourVO = this.getDetail(tour);
		switch (status) {
		case ACTIVE:
			break;
		case COMING:
			if (!currentStatus.equals(TourStatus.COMING.name()))
				//messagePublisher.publishTourChange(JConstants.COMING_EVENT, tourVO);
			break;
		default:
			break;
		}
		this.save(tour);
	}

	public Tour finish(TourRequest request) {
		Integer id = request.getId();
		log.info("Finish Tour " + id);
		Tour tour = this.findOne(id);
		log.info("Get tour by getId [" + id + "]");
		if (tour != null) {
			String status = TourStatus.FINISHED.name();
			tour.setStatus(status);

			TourVO update = new TourVO(id);
			this.save(tour);
			update.setStatus(status);
			return tour;
		} else {
			throw new NotFoundException("Tour Id [" + id + "] invalid ");
		}
	}
}
