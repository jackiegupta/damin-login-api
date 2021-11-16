package vn.vme.service;

import java.io.File;
import java.util.Date;
import java.util.List;
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

import vn.vme.common.JConstants.EventStatus;
import vn.vme.common.Utils;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Event;
import vn.vme.entity.EventGame;
import vn.vme.entity.EventPool;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.EventRequest;
import vn.vme.io.game.EventVO;
import vn.vme.io.game.PoolVO;
import vn.vme.repository.EventGameRepository;
import vn.vme.repository.EventPoolRepository;
import vn.vme.repository.EventRepository;
import vn.vme.repository.UserGiftcodeRepository;

@Service
public class EventService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(EventService.class);

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private EventPoolRepository eventPoolRepository;
	
	@Autowired
	private EventGameRepository eventGameRepository;
	
	
	
	@Autowired
	private UserGiftcodeRepository userGiftcodeRepository;
	
	
	@Autowired
	private StorageService storageService;
	
	//@Autowired(required = false)
	//MessagePublisher messagePublisher;

	@Autowired
	Environment env;

	@Value("${imageUrl}")
	private String imageUrl;
	
	public EventService() {
		log.debug("EventService");
	}

	public EventVO load(EventVO vo) {
		return vo;
	}
	
	public Event findOne(Integer id) {
		return eventRepository.findById(id).orElse(null);
	}

	public Page<Event> findByUserId(Long userId, Pageable paging) {
		return eventRepository.findByUserId(userId, paging);
	}

	public Page<Event> findAll(Pageable pageable) {
		return eventRepository.findAll(pageable);
	}

	public Event save(Event event) {
		return eventRepository.save(event);
	}
	public EventPool savePool(EventPool eventPool) {
		return eventPoolRepository.save(eventPool);
	}

	public void deletePool(Integer eventId) {
		eventPoolRepository.deleteByEventId(eventId);

	}
	public EventGame saveGame(EventGame eventGame) {
		return eventGameRepository.save(eventGame);
	}
	
	public void deleteGame(Integer eventId) {
		eventGameRepository.deleteByEventId(eventId);
		
	}
	public void delete(Integer id) {
		eventRepository.deleteById(id);
		
	}

	
	public Event create(EventRequest request) {
		//request.setStatus(EventStatus.ACTIVE.name());
		Event event = new Event(request);
		Long adminId = UserContextHolder.getContext().getCurrentUser().getId();
		event.setCreateId(adminId);
		event.setStartDate(event.getStartDate());
		event.setCreateDate(new Date());
		event.setGiftCount(0);
		//event.setGame(gameRepository.findById(request.getGameId()).get());
		//Set<Member> userList = new HashSet<Member>(1);
		//userList.add(new Member(adminId, adminId));
		return this.save(event);
	}
	

	public List<Event> findByStatus(String status) {
		return eventRepository.findByStatus(status);
	}

	public Page<Event> findSuggestEvent(Pageable paging) {
		return eventRepository.findSuggestEvent(paging);
	}

	public Page<Event> findByStatusAndStartDateLessThan(String status, Date next2Days, Pageable paging) {
		return eventRepository.findByStatusAndStartDateLessThan(status, next2Days, paging);
	}

	public List<Event> findByStatusAndStartDateLessThan(String status, Date next2Days) {
		return eventRepository.findByStatusAndStartDateLessThan(status, next2Days);
	}

	public Page<Event> listHomeEvent(String status, Pageable paging) {
		return eventRepository.findByStatus(status, paging);
	}

	public EventVO getDetail(Integer id) {
		return getDetail(findOne(id));
	}

	public EventVO getDetail(Event event) {
		EventVO eventVO = event.getVO();
		//Total count user pool (giftcode)
		List<PoolVO> pools = eventVO.getPools();
		for (PoolVO poolVO : pools) {
			int poolId = poolVO.getId();
			int count = userGiftcodeRepository.countByPoolId(poolId);
			poolVO.setCount(count);
		}
		return eventVO;
	}
	public Event storePhoto(Event existed, MultipartFile file) {
		Integer id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = Event.class.getSimpleName().toLowerCase();
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
	public void changeStatus(Event event, EventStatus status) {
		event.setStatus(status.name());
		String currentStatus = event.getStatus();
		EventVO eventVO = this.getDetail(event);
		switch (status) {
		case ACTIVE:
			break;
		case COMING:
			if (!currentStatus.equals(EventStatus.COMING.name()))
			break;
		case LIVE:
			if (!currentStatus.equals(EventStatus.LIVE.name()))
			break;
		default:
			break;
		}
		this.save(event);
	}

	public Event finish(EventRequest request) {
		Integer id = request.getId();
		log.info("Finish Event " + id);
		Event event = this.findOne(id);
		log.info("Get user by getId [" + id + "]");
		if (event != null) {
			String status = EventStatus.FINISHED.name();
			event.setStatus(status);

			EventVO update = new EventVO(id);
			this.save(event);
			update.setStatus(status);
			return event;
		} else {
			throw new NotFoundException("Event Id [" + id + "] invalid ");
		}
	}
}
