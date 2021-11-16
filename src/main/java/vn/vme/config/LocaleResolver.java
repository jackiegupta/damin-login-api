package vn.vme.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * https://josdem.io/techtalk/spring/spring_boot_internationalization/
 *
 */
//@Component
public class LocaleResolver extends AcceptHeaderLocaleResolver {

  private static final List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("vi"));

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String language = request.getHeader("Accept-Language");
    log.info("language: " + language);
    if (language == null || language.isEmpty()) {
      return Locale.getDefault();
    }
    List<Locale.LanguageRange> list = Locale.LanguageRange.parse(language);
    Locale locale = Locale.lookup(list, LOCALES);
    return locale;
  }

}