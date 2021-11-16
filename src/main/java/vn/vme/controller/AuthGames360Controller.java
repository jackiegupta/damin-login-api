package vn.vme.controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import vn.vme.entity.User;
import vn.vme.io.rest360.User360;
import vn.vme.io.user.UserRequest;
import vn.vme.service.Rest360Service;
import vn.vme.service.UserService;

import java.util.Base64;

@RestController
@RequestMapping("/auth")
public class AuthGames360Controller extends BaseController {
    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Rest360Service rest360Service;

    @Value("${accessTokenUri}")
    private String accessTokenUri;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody User user) throws Exception {

        User360 user360 = rest360Service.login(user);

        if (user360 == null) {
            response("login error: ", user360.getRequestId());
        }

        UserRequest userRequest = new UserRequest();
        userRequest.setId(user360.getId());
        userRequest.setUserName(user.getUserName());
        userRequest.setPassword(user.getPassword());
        userService.createUserNotOTP(userRequest);

        String basicToken = env.getProperty("jwt.clientId") + ":" + env.getProperty("jwt.clientSecret");
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(basicToken.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", basicAuth);
        HttpEntity<String> header = new HttpEntity<String>(headers);
        String url = accessTokenUri;
        url += "?grant_type=password";
        url += "&username=" + user.getUserName();
        url += "&password=" + user.getPassword();
        ResponseEntity response = this.restTemplate.postForEntity(url, header, Object.class);

//        if (response)
        System.out.println(response);

        return response(response.getBody());
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@RequestBody User user) throws Exception {
        User360 user360 = rest360Service.register(user);
        if (user360 == null) {
            return response("register error", user.getUserName());
        }
        UserRequest userRequest = new UserRequest();
        userRequest.setId(user360.getId());
        userRequest.setUserName(user.getUserName());
        userRequest.setPassword(user.getPassword());
        userService.createUserNotOTP(userRequest);

        return response(user360);
    }
}

