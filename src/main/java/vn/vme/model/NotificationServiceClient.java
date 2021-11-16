package vn.vme.model;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import vn.vme.common.JConstants;
import vn.vme.common.URI;
import vn.vme.io.notify.NotificationRequest;
import vn.vme.io.notify.NotificationVO;

@Service
public class NotificationServiceClient {
	static Logger log = LoggerFactory.getLogger(NotificationServiceClient.class.getName());
	@Value("${serviceUrl}")
	String gateway;
	@Autowired
	RestTemplate rest;
	

	private String url;
	
	@PostConstruct
	private void init() {
		this.url = gateway + JConstants.NOTIFY_API + URI.V1;
	}
	
	public NotificationVO createNotification(@RequestBody NotificationRequest request) {
		return rest.postForEntity(url + URI.NOTIFICATION, request, NotificationVO.class).getBody();
	}
	
	public NotificationVO createBroadcastNotification(@RequestBody NotificationRequest request) {
		return rest.postForEntity(url + URI.ALL, request, NotificationVO.class).getBody();
	}
	
	public List<NotificationVO> createBatchNotification(@RequestBody NotificationRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(request,headers);
		ResponseEntity<List<NotificationVO>> res = rest.exchange(url + URI.LIST, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<NotificationVO>>() {});
		
		return res.getBody();
	}
	

}
