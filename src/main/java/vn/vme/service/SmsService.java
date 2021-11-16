package vn.vme.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import vn.vme.common.DateUtils;
import vn.vme.common.JConstants.Status;
import vn.vme.common.Utils;
import vn.vme.entity.Sms;
import vn.vme.exception.SmsException;
import vn.vme.repository.SmsRepository;

@Service
public class SmsService {
	private static final Logger log = LoggerFactory.getLogger(SmsService.class);

	@Autowired
	private SmsRepository smsRepository;

	public SmsService() {
		
	}
	@Value("${aws.accessKeyId}")
	String accessKeyId;

	@Value("${aws.secretAccessKey}")
	String secretAccessKey;
	
	@Value("${aws.region}")
	String region;
	
	@Value("${sms.sender}")
	String sender;
	
	@Value("${sms.senderName}")
	String senderName;
	
	@Value("${sms.limitCheckMinutes}")
	int limitCheckMinutes;
	@Value("${sms.adminPhone}")
	String adminPhone;
	
	@Autowired
	Environment env;
	
	public AmazonSNS getSmsClient() throws SmsException {
		try {
			AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
			AmazonSNS client = AmazonSNSClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(credentials))
					.withRegion(region)
					.build();
			return client;
		} catch (Exception e) {
			log.error(accessKeyId + " cause " + e.getMessage());
			throw e;
		}
	}

	public void sendSMSMessage(AmazonSNS snsClient, String message, String phoneNumber,
			Map<String, MessageAttributeValue> smsAttributes) throws SmsException {
		PublishRequest publish = new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber)
				.withMessageAttributes(smsAttributes);

		PublishResult result = snsClient.publish(publish);
		log.info("Has send SMS Message [" + Utils.mask(phoneNumber) + "] " + result.getMessageId());

	}
	public Sms findOne(Long id) {
		return smsRepository.findById(id).orElseGet(null);
	}

	public Sms findByPhone(String sms) {
		return smsRepository.findByPhone(sms);
	}
	
	public Page<Sms> findAll(Pageable pageable) {
		return smsRepository.findAll(pageable);
	}
	public void send(String phoneNumber, String message) throws SmsException {
		log.debug("Sending smsVerification " + phoneNumber);
		phoneNumber = Utils.phoneVN(phoneNumber);
		try {
			AmazonSNS client = getSmsClient();
			Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
			// <set SMS attributes>
			boolean isAdminPhone = phoneNumber.equals(Utils.phoneVN(adminPhone));
			if (!isSmsDeny(phoneNumber)) {
				if (Utils.isTest(env) && !isAdminPhone) {
					log.warn("Local skip sending SMS Message " + Utils.mask(phoneNumber) + "," + message);
				} else {
					sendSMSMessage(client, message, phoneNumber, smsAttributes);
				}
			}else {
				log.error("==================== SMS DENY serious in "+ limitCheckMinutes);
			}
			this.save(phoneNumber, message);
			
		} catch (Exception ex) {
			log.error("SNS send failed", ex);
			throw new SmsException("Exception occured during the message of the invitation", ex);
		}
	}

	// Check send limit SMS for serious price
	public boolean isSmsDeny(String phone) throws SmsException{
		Date createDateAgo = DateUtils.addMinutes(0 - limitCheckMinutes);
		int count = smsRepository.countByCreateDateGreaterThan(createDateAgo);
		boolean warning = count >= limitCheckMinutes;
		boolean serious = count >= 2 * limitCheckMinutes;
		if(warning && !serious) {
			AmazonSNS client = getSmsClient();
			Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
			String message = "LIMITED SMS in " + limitCheckMinutes + " minutes at " + phone + " serious " + serious;
			sendSMSMessage(client, message, Utils.phoneVN(adminPhone),	smsAttributes);
			this.save(adminPhone, message);
		}
		
		return serious;
		
	}
	private void save(String phoneNumber, String message) {
		Sms sms = new Sms(phoneNumber, message);
		sms.setCreateDate(new Date());
		sms.setSender("aws sns");
		sms.setStatus(Status.SUCCESS.name());
		this.save(sms);
		
	}
	public Sms save(Sms sms) {
		boolean isNew =Utils.isNotEmpty(sms.getId());
		log.debug("save " + isNew);
		sms = smsRepository.save(sms);
		return sms;
	}

	public void delete(Long id) {
		smsRepository.deleteById(id);
	}
}
