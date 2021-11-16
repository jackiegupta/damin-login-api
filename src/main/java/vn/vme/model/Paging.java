package vn.vme.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Paging {

	public static final int PAGE = 1;
	public static final int SIZE = 10;
	
	public static final String SORT =  "id";
	public static final String DESC =  Direction.DESC.name();
	
	private int page = PAGE;
	private int size = SIZE;
	private long totalRows = -1;
	private int totalPages = -1;

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		if (size >= 1) {
			this.size = size;
		}
		
	}
	
	public Paging() {
	}
	public Paging(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page >= 1) {
			this.page = page;
		}
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	/**
	 * Validate and set default page request
	 * @param page
	 * @param size
	 * @param sortDirection
	 * @param sortProperty
	 * @return
	 */
	@JsonIgnore
	public Pageable getPageRequest(int page, int size) {
		setPage(page);
		setSize(size);
		Pageable pageRequest = PageRequest.of(this.page - 1, this.size);
		return pageRequest;
	}
	@JsonIgnore
	public Pageable getPageRequest(int page, int size, String sortDirection, String sortProperty) {
		setPage(page);
		setSize(size);
		if (StringUtils.isEmpty(sortDirection)) {
			sortDirection = Direction.DESC.name();
		}
		if (StringUtils.isEmpty(sortProperty)) {
			sortProperty = "id";
		}
		Sort sort = Sort.by(Direction.valueOf(sortDirection), sortProperty);
		Pageable pageRequest = PageRequest.of(this.page - 1, this.size, sort);
		return pageRequest;
	}
	@JsonIgnore
	public Pageable getPageRequest(int page,int size, Order[] orders) {
		setPage(page);
		setSize(size);
		Sort sort = Sort.by(orders);
		Pageable pageRequest = PageRequest.of(this.page - 1, this.size, sort);
		return pageRequest;
	}
	@JsonIgnore
	public Pageable getPageRequest(Order[] orders) {
		setPage(page);
		setSize(size);
		Sort sort = Sort.by(orders);
		Pageable pageRequest = PageRequest.of(this.page - 1, this.size, sort);
		return pageRequest;
	}
	public String toString(){
		return  " Page:["+ page +"] Limit:["+size+"]"+" TotalPages:["+getTotalPages()+"]"+" TotalRows:["+totalRows+"]";
	}
	/**
	 * Default
	 * @return
	 */
	@JsonIgnore
	public Pageable getDefault() {
		return getPageRequest(page, size, null, null);
	}
	
}
