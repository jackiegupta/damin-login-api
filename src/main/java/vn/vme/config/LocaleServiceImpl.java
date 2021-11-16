package vn.vme.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

//@Service
public class LocaleServiceImpl implements LocaleService {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private LocaleResolver localeResolver;

	public String getMessage(String code, HttpServletRequest request) {
		return messageSource.getMessage(code, null, localeResolver.resolveLocale(request));
	}

}