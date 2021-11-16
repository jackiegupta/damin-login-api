package vn.vme.service;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import vn.vme.common.JConstants.Status;
import vn.vme.common.Utils;
import vn.vme.entity.Item;
import vn.vme.entity.News;
import vn.vme.io.game.ItemRequest;
import vn.vme.io.game.ItemVO;
import vn.vme.repository.ItemRepository;

@Service
public class ItemService {
	private static final Logger log = LoggerFactory.getLogger(ItemService.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@Value("${imageUrl}")
	private String imageUrl;

	@Value("${serviceUrl}")
	private String serviceUrl;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RestTemplate restTemplate;
	public ItemService() {
	}
	
	public ItemVO load(ItemVO vo) {
		return vo;
	}
	public Item findOne(Long id) {
		return itemRepository.findById(id).get();
	}
	public Page<Item> search(ItemRequest request, Pageable pageable) {
		
		return itemRepository.search(request, pageable);
	}
	
	public Iterable<Item> findAll() {
		return itemRepository.findAll();
	}
	public Item create(ItemRequest request) {
		request.setStatus(Status.INACTIVE.name());
		Item item = new Item(request);
		item.setCreateDate(new Date());
		this.save(item);
		return item;
	}
	
	public Item update(ItemRequest request, Item existed) throws Exception {
		log.debug("update " + request);
		Item item = new Item(request);
		Utils.copyNonNullProperties(item, existed);
		existed = save(existed);
		return existed;
	}

	public Item save(Item item) {
		Long id = item.getId();
		item = itemRepository.save(item);
		log.debug("save " + item);
		return item;
	}
	
	public void delete(Long id) {
		itemRepository.deleteById(id);
	}
	
	public Item storePhoto(Item existed, MultipartFile file) {
		Long id = existed.getId();
		try {
			String oldFile = existed.getPhoto();
			String fileName = id + "_" + UUID.randomUUID() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			String folder = Item.class.getSimpleName().toLowerCase();
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
}
