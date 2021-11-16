package vn.vme.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

/**
 * @m-tech
 */

public class QueryParam {

	private String query = "";

	public QueryParam(String name, String value) {
		encode(name, value);
	}

	public void add(String name, String value) {
		if (value == null) {
			value = StringUtils.EMPTY;
		}
			
		query += "&";
		encode(name, value);
	}

	private void encode(String name, String value) {
		try {
			query += URLEncoder.encode(name, "UTF-8");
			query += "=";
			query += URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException("Broken VM does not support UTF-8");
		}
	}

	public String getQuery() {
		return query;
	}

	public String toString() {
		return getQuery();
	}

}
