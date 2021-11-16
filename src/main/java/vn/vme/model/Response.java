package vn.vme.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response {

	private Date timestamp;
	private String message;
	private String details;

	public Response(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}
	public Response(String message) {
		super();
		this.timestamp = new Date();
		this.message = message;
	}
}
