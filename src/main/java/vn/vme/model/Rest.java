package vn.vme.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import vn.vme.common.JCode;
import vn.vme.common.JCode.CommonCode;
import vn.vme.common.JConstants;
import vn.vme.common.Utils;

/**
 * Every response content data will include class Rest or Rest: data array 
 * rest{
 *		 "status":"OK",
 *		 "message":"Success", 
 *		 "data":{"property":"value"} } 
 * 
 * rest (ArrayList include total and list data)
 *     { "status":"OK", 
 *       "message":"Success",
 *       "data":{
 *                "total":"10", 
 *                 "list":[ {"p1":"v1"}, 
 *                          {"p2":"v2"} 
 *                        ]
 *               }
 *      }
 */
@Getter
@Setter
public class Rest<T> implements Serializable {
	protected static Logger log = LoggerFactory.getLogger(Rest.class.getName());
	private String status;

	private String message;

	private T data;

	public Rest(String code) {
		this.status = code;
	}

	public Rest() {
	}
	
	//set status = 000
	public Rest(T data) {
		this.status = JCode.SUCCESS;
		this.message = "Success";
		this.data =  data;
	}
	
	public Rest(String status, T data) {
		this.data =  data;
		this.status = status;
	}
	public Rest(String status, String message) {
		this.status = status;
		this.message =  message;
	}
	/*
	public Rest(HttpStatus httpStatus, String message) {
		this.status = (httpStatus.value() == HttpStatus.OK.value()) ? JCode.SUCCESS : String.valueOf(httpStatus.value());
		this.message = message;
	}

	public Rest(HttpStatus httpStatus) {
		this.status = (httpStatus.value() == HttpStatus.OK.value()) ? JCode.SUCCESS : String.valueOf(httpStatus.value());
		this.message = httpStatus.name();
	}
*/
	public Rest(Exception e) {
		this.status = CommonCode.SYSTEM_ERROR.code;
		this.message = CommonCode.SYSTEM_ERROR.code;
	}

	public Rest(HttpStatus httpStatus, Response errorDetails) {
		this.status = String.valueOf(httpStatus.value());
		this.data = (T) errorDetails;
	}


	public String toString() {
		return status + "," + message;
	}
	/**
	 * Convert data list from JPA result
	 * @param responseList 
	 * @param result
	 */
	public void setDataList(List<?> responseList, Page result) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(JConstants.DATA_LIST, responseList);
		Paging paging = new Paging(result.getNumber() +1, result.getSize());
		paging.setTotalPages(result.getTotalPages());
		paging.setTotalRows(result.getTotalElements());
		map.put(Paging.class.getSimpleName().toLowerCase(), paging);
		this.data = (T) map;

	}

	public void setDataList(List<?> responseList, PageRequest page) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(JConstants.DATA_LIST, responseList);
		//TDOO PAGAING
		Paging paging = new Paging(page.getPageNumber() +1, page.getPageSize());
		//paging.setTotalPages(result.getTotalPages());
		//paging.setTotalRows(result.getTotalElements());
		map.put(Paging.class.getSimpleName().toLowerCase(), paging);
		this.data = (T) map;

	}
	@JsonIgnore
	public boolean isSuccess() {
		return Utils.isNotEmpty(status) && status.equals(JCode.SUCCESS); 
	}
	
	
}
