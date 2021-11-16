package vn.vme;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import vn.vme.common.SSLUtil;
import vn.vme.config.SwaggerConfig;

@SpringBootApplication
@EnableResourceServer
@EnableAuthorizationServer
@EnableTransactionManagement
@EnableScheduling
@EnableFeignClients
@RestController
@Import(SwaggerConfig.class)
public class VmeGateApplication {
    private static final Logger log = LoggerFactory.getLogger(VmeGateApplication.class);
    @Autowired
    Environment env;
    @Value("${server.port}")
    String port;

    @Value("${key-store-password}")
    String password;

    @PostConstruct
    public void init() {
        log.info("INIT SpringApplication env [" + env.getActiveProfiles()[0] + "] at port [" + port + "] ");        
    }

    public static void main(String[] args) {
    	//System.setProperty("spring.devtools.restart.enabled", "true");
        SpringApplication.run(VmeGateApplication.class, args);
    }

    //@LoadBalanced
    @Bean
    public RestTemplate restTemplate() throws Exception {
        return SSLUtil.getRestTemplate(password);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
