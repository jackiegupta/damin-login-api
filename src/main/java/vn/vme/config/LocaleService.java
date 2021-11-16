package vn.vme.config;

import javax.servlet.http.HttpServletRequest;

public interface LocaleService {
	String getMessage(String code, HttpServletRequest request);
}