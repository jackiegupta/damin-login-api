package vn.vme.config;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {
	private static final Logger log = LoggerFactory.getLogger(SwaggerConfig.class.getName());
	@Value("${spring.application.name}")
	private String applicationName;
	@Value("${accessTokenUri}")
	private String accessTokenUri;	
	
	@Value("${jwt.clientId:}")
	private String jwtClientId;
	
	@Value("${jwt.clientSecret:}")
	private String jwtClientSecret;
	@Value("${jwt.expire:0}")
	private int expire;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("PUT", "DELETE", "POST", "GET", "PATCH").allowCredentials(false).maxAge(30*24*60*60);
	}

	@Bean
	public Docket api() throws Exception {
		List<ResponseMessage> list = new ArrayList<ResponseMessage>();
		list.add(new ResponseMessageBuilder().code(500).message("500 message").responseModel(new ModelRef("Error")).build());
		list.add(new ResponseMessageBuilder().code(403).message("Forbidden!").build());
		
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		
		return 	docket.useDefaultResponseMessages(false).globalResponseMessage(RequestMethod.GET, list)
				.ignoredParameterTypes(ApiIgnore.class).select()
				.apis(RequestHandlerSelectors.basePackage("vn.vme.controller"))
				.paths(PathSelectors.any())
				.build().securityContexts(Collections.singletonList(securityContext()))
				.securitySchemes(Arrays.asList(securitySchema()))
				.apiInfo(apiInfo());
	}
	private OAuth securitySchema() {

		List<AuthorizationScope> authorizationScopeList = new ArrayList();

		List<GrantType> grantTypes = new ArrayList();
		GrantType passwordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(accessTokenUri);
		grantTypes.add(passwordCredentialsGrant);

		return new OAuth("oauth2", authorizationScopeList, grantTypes);
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {

		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
		authorizationScopes[0] = new AuthorizationScope("read", "read all");
		authorizationScopes[1] = new AuthorizationScope("trust", "trust all");
		authorizationScopes[2] = new AuthorizationScope("write", "write all");

		return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
	}

	@Bean
	public SecurityConfiguration security() {
		return new SecurityConfiguration(jwtClientId, jwtClientSecret,  "", "", "", ApiKeyVehicle.HEADER, "", " ");
	}
	private ApiInfo apiInfo() {
		log.info("Api Info " + applicationName);
		ApiInfoBuilder apiInfo = new ApiInfoBuilder();
		apiInfo.title("API");
		
		return apiInfo.version("1.0.0").license("license").build();
	}

	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource msg = new ReloadableResourceBundleMessageSource();
	    msg.setBasename("classpath:messages");
	    msg.addBasenames("classpath:common");
	    msg.setDefaultEncoding("UTF-8");
	    return msg;
	}
	/*
	@Bean
	public LocalValidatorFactoryBean getValidator() {
	    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	    bean.setValidationMessageSource(messageSource());
	    return bean;
	}
	/*
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);
		return sessionLocaleResolver;
	}
  	*/
}
