package vn.vme.io.rest360;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rest360<T> implements Serializable {

	private String username;
	private String pswd;
	@JsonProperty("error_code")
	private int errorCode;
	@JsonProperty("error_desc")
	private String errorDesc;
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("request_id")
	private String requestId;
	private T data;
	public Rest360() {
	}

	public Rest360(String username, String pswd) {
		this.username = username;
		this.pswd = pswd;
	}

	public String toString() {
		return errorCode + "," + errorDesc;
	}
}
