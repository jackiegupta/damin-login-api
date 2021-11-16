package vn.vme.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@Configuration
public class I18nService {

	@Autowired
	protected MessageSource msg;

	public String msg(String code) {
		try {
			return msg.getMessage(code, null, new Locale("vi"));
		} catch (Exception e) {
			return code;
		}
	}

	public String msg(String code, Object param) {
		try {
			return msg.getMessage(code, new String[] { param.toString() }, new Locale("vi"));
		} catch (Exception e) {
			return code + " " + param;
		}
	}

	public String msg(String code, Object[] params) {
		try {
			return msg.getMessage(code, params, new Locale("vi"));
		} catch (Exception e) {
			return code;
		}
	}
}
