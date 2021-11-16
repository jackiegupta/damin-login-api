package vn.vme.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.URI;
import vn.vme.context.UserContextHolder;
import vn.vme.entity.Item;
import vn.vme.entity.UserItem;
import vn.vme.exception.NotFoundException;
import vn.vme.io.game.ItemRequest;
import vn.vme.io.game.ItemVO;
import vn.vme.io.game.UserItemRequest;
import vn.vme.io.user.UserVO;
import vn.vme.model.Paging;
import vn.vme.model.Response;
import vn.vme.repository.ItemRepository;
import vn.vme.repository.UserItemRepository;
import vn.vme.service.ItemService;
import vn.vme.service.StorageService;

@RestController
@RequestMapping(URI.V1 + URI.ITEM)
public class ItemController extends BaseController {

	static Logger log = LoggerFactory.getLogger(ItemController.class.getName());
	@Autowired
	public ItemService itemService;
	
	@Autowired
	public ItemRepository itemRepository;
	
	@Autowired
	public UserItemRepository userItemRepository;
	
	@Autowired
	public StorageService storageService;

	@ApiOperation(value = "Register new item")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Item successfully registered ", response = HashMap.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Item already Registered with Phone Number and Email but not yet verify", response = HashMap.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Item already registered also verified. Go signin.", response = HashMap.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 409, message = "CONFLICT, Phone is used, as in the response", response = HashMap.class),
			@ApiResponse(code = 423, message = "LOCKED", response = ItemVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity registerItem(@RequestBody ItemRequest request)
			throws Exception {
		log.info("Item registration: " + request);
		super.validateRequest(request);
		Item item = itemService.create(request);
		return response(item.getVO());
	}
	
	@ApiOperation(value = "Create new general item")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Item successfully registered ", response = ItemVO.class),
			@ApiResponse(code = 202, message = "ACCEPTED, Item already Registered with Phone Number and Domain name but not yet verify", response = ItemVO.class),
			@ApiResponse(code = 208, message = "ALREADY_REPORTED, Item already registered also verified. Go signin.", response = ItemVO.class),
			@ApiResponse(code = 226, message = "IM_USED, Email is used, as in the response", response = ItemVO.class),
			@ApiResponse(code = 409, message = "CONFLICT, ItemName is used, as in the response", response = ItemVO.class),
			@ApiResponse(code = 423, message = "LOCKED", response = ItemVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity createItem(@ModelAttribute ItemRequest request)
			throws Exception {
		log.info("Creating item: " + request);
		Item item = itemService.create(request);
		if(item != null) {
			item = uploadFile(request, item);
		}
		return response(item.getVO());
	}
	
	@ApiOperation(value = "Update item")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of item update", response = ItemVO.class),
			@ApiResponse(code = 404, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN' ,'GM')")
	public ResponseEntity updateItem(@ApiParam(value = "Object json item") @ModelAttribute ItemRequest request)
			throws Exception {

		Long id = request.getId();
		log.info("Update Item:" + request);
		Item existed = itemService.findOne(id);
		if (existed == null) {
			throw new NotFoundException("Item Id [" + id + "] invalid ");
		}
		
		existed = itemService.update(request, existed);
		existed = uploadFile(request, existed);
		log.info("Patch Item with existed = " + existed);
		return response(existed.getVO());
	}
	
	private Item uploadFile(ItemRequest request, Item news) {
		MultipartFile file = request.getFile();
		if (file != null && !file.isEmpty() && !file.getOriginalFilename().contains("assets/images/tmp.png")) {
			news = itemService.storePhoto(news,file);
		}
		return news;
	}	

	@ApiOperation(value = "Get Item by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return item response  ", response = ItemVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getItem(@ApiParam(value = "Get item by Id", defaultValue = "1") 
								  @PathVariable(name = "id") Long id) throws Exception {
		log.info("Get item by id [" + id + "]");
		Item item = itemService.findOne(id);
		if (item != null) {
			return response(itemService.load(item.getVO()));
		} else {
			throw new NotFoundException("Item Id [" + id + "] invalid ");
		}
	}

	@ApiOperation(value = "Get Item by user id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return item response  ", response = ItemVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listUserItem(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Long gameId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Long itemId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		UserVO user = UserContextHolder.getContext().getCurrentUser();
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		UserItemRequest request = new UserItemRequest(key, user.getId(), itemId, status, "", "");
		Page<UserItem> result = userItemRepository.search(request, paging);
		log.info("Search item total elements [" + result.getTotalElements() + "]");
		List<UserItem> contentList = result.getContent();
		List<ItemVO> responseList = new ArrayList<ItemVO>();

		for (UserItem userItem : contentList) {
			responseList.add(itemService.load(userItem.getItem().getVO()));
		}
		return responseList(responseList, result);
	}
	
	@ApiOperation(value = "List item by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = ItemVO.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listItem(
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String key,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") Long userId,
			@ApiParam(defaultValue = "") @RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "DESC") String sortDirection,
			@RequestParam(required = false, defaultValue = "id") String sortProperty) throws Exception {
		log.info("Search Item");
		
		Pageable paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		ItemRequest request = new ItemRequest(key,userId, status);
		Page<Item> result = itemRepository.search(request, paging);
		log.info("Search item total elements [" + result.getTotalElements() + "]");
		List<Item> contentList = result.getContent();
		List<ItemVO> responseList = new ArrayList<ItemVO>();

		for (Item item : contentList) {
			responseList.add(itemService.load(item.getVO()));
		}
		return responseList(responseList, result);
	}

	
	@ApiOperation(value = "Delete item")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted item", response = Response.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Item fullName. Go signin.", response = Response.class),
			@ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Response.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize ("hasAnyAuthority('ROOT', 'ADMIN')")
	public ResponseEntity deleteItem(@ApiParam(value = "By Id", defaultValue = "1") @PathVariable(required = true) Long id) throws Exception {
		log.info("Delete  Item [" + id + "]");
		Item item = itemService.findOne(id);
		if (item != null) {
			log.info("Delete  Item [" + item.getId() + "]");
			itemService.delete(item.getId());
			return responseMessage("Deleted  Item [" + item.getId() + "] sucessfully");
		} else {
			throw new NotFoundException("Item id [" + id + "] invalid ");
		}
	}

	
}
