package vn.vme.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.SendResponse;

import vn.vme.common.JConstants;
import vn.vme.common.JConstants.ResponseStatus;
import vn.vme.entity.Notify;
import vn.vme.io.notify.NotificationRequest;
import vn.vme.io.user.UserVO;
import vn.vme.repository.NotifyRepository;
import vn.vme.repository.UserRepository;

@Service
@Transactional
public class NotifyService {

	private static final Logger logger = LoggerFactory.getLogger(NotifyService.class);

	@Autowired
	UserRepository userServiceClient;

	@Autowired
	NotifyRepository notifyRepository;

	@PostConstruct
	public void initialize() {
		try {
			FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(
					GoogleCredentials.fromStream(new ClassPathResource(JConstants.FIREBASE_ACCOUNT).getInputStream()))
					.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				logger.info("Firebase application has been initialized");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
		return FirebaseMessaging.getInstance().sendAsync(message).get();
	}

	private String sendData(Map<String, String> data, String topic) throws InterruptedException, ExecutionException {

		Message message = Message.builder().putAllData(data).setTopic(topic).build();

		String response = sendAndGetResponse(message);
		logger.info("Sent data. Topic: " + topic + ", " + response);

		return response;
	}

	public void save(Notify notify) {
		notifyRepository.save(notify);
	}

	public Notify sendMessageToAll(NotificationRequest request) {

		Notify notify = new Notify();
		notify.setTitle(request.getTitle());
		notify.setShortContent(request.getShortContent());
		notify.setFullContent(request.getFullContent());
		if(request.getId() != null) {
			notify.setId(request.getId());
		}
		notify.setUserId(0l);
		notify.setType(request.getType());
		notify.setTarget(request.getTarget());
		if(notify.getCreateDate() == null) {
			notify.setCreateDate(new Date());
		}
		notify.setUpdateDate(new Date());
		try {
			Map<String, String> pushData = new HashMap<>();
			pushData.put("req", JConstants.NOTIFICATION_REQ_MESSAGE);
			pushData.put("title", request.getTitle()==null?"": request.getTitle());
			pushData.put("short_content", request.getShortContent()==null?"":request.getShortContent());
			pushData.put("full_content", request.getFullContent()==null?"":request.getFullContent());
			pushData.put("type", request.getType()== null ? "": request.getType() );
			pushData.put("target", request.getTarget() == null ? "": request.getTarget());
			pushData.put("photo", request.getPhoto() == null ? "": request.getPhoto());
			pushData.put("user_id", "0");
			

			sendData(pushData, JConstants.NOTIFICATION_SUBSCRIBE_TOPIC);
			notify.setStatus(ResponseStatus.SUCCESS.name());
		} catch (Exception e) {
			logger.error(e.getMessage());
			notify.setStatus(ResponseStatus.FAILED.name());
		}
		save(notify);
		return notify;

	}

	public Notify sendMessageToDevice(NotificationRequest request) {

		Notify notify = new Notify();
		notify.setTitle(request.getTitle());
		notify.setShortContent(request.getShortContent());
		notify.setFullContent(request.getFullContent());
		if(request.getId() != null) {
			notify.setId(request.getId());
		}
		notify.setUserId(request.getUserId());
		notify.setType(request.getType());
		notify.setTarget(request.getTarget());
		notify.setPhoto(request.getPhoto());
		if(notify.getCreateDate() == null) {
			notify.setCreateDate(new Date());
		}
		notify.setUpdateDate(new Date());
		try {
			Map<String, String> pushData = new HashMap<>();
			pushData.put("req", JConstants.NOTIFICATION_REQ_MESSAGE);
			pushData.put("title", request.getTitle()==null?"": request.getTitle());
			pushData.put("short_content", request.getShortContent()==null?"":request.getShortContent());
			pushData.put("full_content", request.getFullContent()==null?"":request.getFullContent());
			pushData.put("type", request.getType()== null ? "": request.getType() );
			pushData.put("target", request.getTarget() == null ? "": request.getTarget());
			pushData.put("photo", request.getPhoto() == null ? "": request.getPhoto());
			pushData.put("user_id", String.valueOf((notify.getUserId()== null ? 0: notify.getUserId())));
			
			UserVO userVO = userServiceClient.findById(request.getUserId()).get().getVO();
			if (userVO != null) {
				if (userVO.getDeviceId() != null && !userVO.getDeviceId().isEmpty()) {
					Message msg = Message.builder().putAllData(pushData).setToken(userVO.getDeviceId()).build();

					String response = sendAndGetResponse(msg);

					notify.setStatus(ResponseStatus.SUCCESS.name());
					logger.info("Sent data. User: " + userVO.getUserName() + ", response = " + response);
				} else {
					logger.error("Not firebase device id found " + request.getUserId());
					notify.setStatus(ResponseStatus.FAILED.name());
				}
			} else {
				logger.error("Not user found " + request.getUserId());
				notify.setStatus(ResponseStatus.FAILED.name());

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			notify.setStatus(ResponseStatus.FAILED.name());
		}
		save(notify);
		return notify;
	}

	public List<Notify> sendMessageToDevices(NotificationRequest request) {
		List<Notify> notifies = new ArrayList<Notify>();
         
		try {
			Map<String, String> pushData = new HashMap<>();
			pushData.put("req", JConstants.NOTIFICATION_REQ_MESSAGE);
			pushData.put("title", request.getTitle()==null?"": request.getTitle());
			pushData.put("short_content", request.getShortContent()==null?"":request.getShortContent());
			pushData.put("full_content", request.getFullContent()==null?"":request.getFullContent());
			pushData.put("type", request.getType()== null ? "": request.getType() );
			pushData.put("target", request.getTarget() == null ? "": request.getTarget());
			pushData.put("photo", request.getPhoto() == null ? "": request.getPhoto());
			
			List<Message> messages = new ArrayList<Message>();
			List<UserVO> userVOs = new ArrayList<UserVO>();
			if(request.getUserIds() != null && !request.getUserIds().isEmpty()) {
				for(Long id: request.getUserIds()) {
					try {
						UserVO userVO = userServiceClient.findById(id).get().getVO();
						if (userVO != null) {
							pushData.put("user_id", String.valueOf(userVO.getId()));
							if (userVO.getDeviceId() != null && !userVO.getDeviceId().isEmpty()) {
								Message msg = Message.builder().putAllData(pushData).setToken(userVO.getDeviceId()).build();
								messages.add(msg);
								userVOs.add(userVO);
							} else {
								logger.error("Not firebase device id found " + userVO.getId());
							}
						} else {
							logger.error("Not user found " + id);
						}
					}catch(Exception ex) {
						logger.error(ex.getMessage());
					}
				}
			}
			if(!messages.isEmpty()) {
				BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
                logger.debug("Finish to send batch of notification messages("+messages.size()+ "devices): success_count="+response.getSuccessCount()+" failure_count="+response.getFailureCount());

                if(response != null && response.getResponses() != null){
                    List<SendResponse> sendResponses = response.getResponses();
                    for(int i=0;i<userVOs.size();i++){
                        try{
                            UserVO requestUser = userVOs.get(i);
                            SendResponse sendResponse = sendResponses.get(i);
                            if(requestUser != null && sendResponse != null){
                            	Notify notify = new Notify();
                        		notify.setTitle(request.getTitle());
                        		notify.setShortContent(request.getShortContent());
                        		notify.setFullContent(request.getFullContent());
                        		notify.setId(request.getId());
                        		notify.setUserId(requestUser.getId());
                        		notify.setPhoto(request.getPhoto());
                        		notify.setCreateDate(new Date());
                        		notify.setUpdateDate(new Date());
                        		
                                if(sendResponse.isSuccessful()){
                            		notify.setStatus(ResponseStatus.SUCCESS.name());
                                }else{
                                	notify.setStatus(ResponseStatus.FAILED.name());
                                }
                                notifies.add(notify);
                            }
                        }catch(Exception ex){
                            logger.error("Failed check batch response", ex);
                        }
                    }
                }
                if(!notifies.isEmpty()) {
                	//notifyRepository.save(notifications);
                }
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return notifies;
		}
		return notifies;
	}
	
	public Page<Notify> findCommonNotifications(Pageable pageable) {
		return notifyRepository.findCommonNotifications(pageable);
	}
	public Page<Notify> findByUser(Long userId, Pageable pageable) {
		return notifyRepository.findByUserId(userId, pageable);
	}

}
