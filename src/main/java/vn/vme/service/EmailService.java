package vn.vme.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.mchange.io.FileUtils;

import vn.vme.common.Utils;
import vn.vme.entity.Email;
import vn.vme.exception.EmailException;
import vn.vme.repository.EmailRepository;
import vn.vme.ses.AWSAttachment;
import vn.vme.ses.AWSEmail;
import vn.vme.ses.SESFrom;

@Service
public class EmailService {
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

	 public static String OTP_EMAIL_SUBJECT = "Gaka OTP";
	    public static String OTP_EMAIL_BODY = "otp của bạn là: ";
	    
	@Autowired
	private EmailRepository emailRepository;

	public EmailService() {

	}

	@Value("${aws.accessKeyId}")
	String accessKeyId;

	@Value("${aws.secretAccessKey}")
	String secretAccessKey;

	@Value("${aws.region}")
	String region;

	@Value("${email.sender}")
	String sender;

	@Value("${email.senderName}")
	String senderName;

	
	public Email findOne(Long id) {
		return emailRepository.findById(id).get();
	}

	public Email findByReceiver(String email) {
		return emailRepository.findByReceiver(email);
	}

	public Page<Email> findAll(Pageable pageable) {
		return emailRepository.findAll(pageable);
	}

	public Email save(Email email) {

		email = emailRepository.save(email);

		return email;
	}

	public void delete(Long id) {
		emailRepository.deleteById(id);
	}

	public AmazonSimpleEmailService getEmailClient() throws EmailException {
		AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
		return client;

	}

	public void send(String to, String subject, String body) throws EmailException {
		send(sender, senderName, Collections.singletonList(to), subject, body);
	}

	@Async
	public void sendOtp(String email, String otp) throws Exception {
		if (Utils.isNotEmpty(email)) {
			send(sender, senderName, Collections.singletonList(email), OTP_EMAIL_SUBJECT, OTP_EMAIL_BODY + otp);
		}
	}
	
	public void send(String from, String senderName, String to, String subject, String body) throws EmailException {
		send(from, senderName, Collections.singletonList(to), subject, body);
	}

	public void send(String from, String senderName, List<String> tos, String subject, String body)
			throws EmailException {
		try {
			AmazonSimpleEmailService client = getEmailClient();
			from = StringUtils.isEmpty(senderName) ? from : String.format("%s <%s>", senderName, from);
			for (String to : tos) {
				Destination destination = new Destination().withToAddresses(new String[] { to });
				Content subjectContent = new Content().withData(subject);
				Content bodyContent = new Content().withData(body);
				Body bodyData = new Body();
				bodyData.setHtml(bodyContent);
				Message message = new Message().withSubject(subjectContent).withBody(bodyData);
				SendEmailRequest request = new SendEmailRequest().withSource(from).withDestination(destination)
						.withMessage(message);
				try {
					client.sendEmail(request);
					log.debug("Sent Email");
				} catch (Exception ex) {
					log.error("Email send failed", ex);
					throw new EmailException("Exception occured during the emailing of a user email", ex);
				}
			}
		} catch (Exception ex) {
			log.error("Email send failed", ex);
			throw new EmailException("Exception occured during the emailing of a user email", ex);
		}
	}

	public void sendAsync(String to, String subject, String body) throws EmailException {
		sendAsync(sender, senderName, Collections.singletonList(to), subject, body);
	}

	public void sendAsync(String from, String senderName, String to, String subject, String body)
			throws EmailException {
		CompletableFuture.runAsync(() -> {
			try {
				send(from, senderName, to, subject, body);
			} catch (EmailException e) {
				e.printStackTrace();
			}
		});

	}

	public void sendAsync(String from, String senderName, List<String> tos, String subject, String body)
			throws EmailException {
		CompletableFuture.runAsync(() -> {
			try {
				send(from, senderName, tos, subject, body);
			} catch (EmailException e) {
				e.printStackTrace();
			}
		});
	}

	public void send(String from, String to, String subject, String body) throws EmailException {
		send(from, null, to, subject, body);
	}

	public void send(String from, List<String> tos, String subject, String body) throws EmailException {
		send(from, null, tos, subject, body);
	}

	public void sendAsync(String from, String to, String subject, String body) throws EmailException {
		sendAsync(from, null, to, subject, body);
	}

	public void sendAsyncMultiple(SESFrom from, String[] tos, String[] cc, String[] bcc, String subject, String body,
			String[] multipartFiles, boolean isHtml, Email email)
			throws EmailException, IOException, MessagingException {

		ArrayList<String> listAddress = new ArrayList<>(Arrays.asList(tos));
		AWSEmail awsEmail = new AWSEmail(listAddress, from, subject, body, isHtml);
		if (cc != null)
			awsEmail.setCc(new ArrayList<>(Arrays.asList(cc)));
		if (bcc != null)
			awsEmail.setBcc(new ArrayList<>(Arrays.asList(bcc)));
		ArrayList<AWSAttachment> awsAttachments = new ArrayList<>();
		for (String filePath : multipartFiles) {
			try {
				File file = new File(filePath);
				AWSAttachment awsAttachment = new AWSAttachment();
				awsAttachment.setName(file.getName());
				awsAttachment.setContent(FileUtils.getBytes(file));
				awsAttachment.setContentType(new MimetypesFileTypeMap().getContentType(file.getName()));
				awsAttachments.add(awsAttachment);
			} catch (Exception e) {
				// TODO: handle exception
				log.info("File not found: "+filePath);
			}
		}
		awsEmail.setFiles(awsAttachments);
		sendAsyncWithFile(awsEmail, email);
	}

	public void sendAsyncMultipleWithFile(SESFrom from, String[] tos, String[] cc, String[] bcc, String subject,
			String body, MultipartFile[] multipartFiles, boolean isHtml, Email email)
			throws EmailException, IOException, MessagingException {

		ArrayList<String> listAddress = new ArrayList<>(Arrays.asList(tos));
		AWSEmail awsEmail = new AWSEmail(listAddress, from, subject, body, isHtml);
		if (cc != null)
			awsEmail.setCc(new ArrayList<>(Arrays.asList(cc)));
		if (bcc != null)
			awsEmail.setBcc(new ArrayList<>(Arrays.asList(bcc)));
		ArrayList<AWSAttachment> awsAttachments = new ArrayList<>();
		if (multipartFiles != null)
			for (MultipartFile file : multipartFiles) {
				AWSAttachment awsAttachment = new AWSAttachment();
				awsAttachment.setName(file.getOriginalFilename());
				awsAttachment.setContent(file.getBytes());
				awsAttachment.setContentType(file.getContentType());
				awsAttachments.add(awsAttachment);
				log.info("Multipart file: name = {}|File content type = {}", file.getOriginalFilename(),
						file.getContentType());
			}
		awsEmail.setFiles(awsAttachments);
		sendAsyncWithFile(awsEmail, email);
	}

	public void sendAsyncWithFile(AWSEmail item, Email email) {
		CompletableFuture.runAsync(() -> {
			try {
				Session session = Session.getDefaultInstance(new Properties());
				MimeMessage message = new MimeMessage(session);
				// set subject
				message.setSubject(item.getSubject(), "UTF-8");
				// set message receiversMailLogger
				message.setFrom(new InternetAddress(item.getFrom().getEmail(), item.getFrom().getName()));
				message.setReplyTo(new Address[] { new InternetAddress(SESFrom.PAYTECH.getEmail()) });
				// set to address
				Address[] addresses = new Address[item.getTo().size()];
				for (int i = 0; i < item.getTo().size(); i++) {
					addresses[i] = new InternetAddress(item.getTo().get(i));
				}
				message.setRecipients(javax.mail.Message.RecipientType.TO, addresses);
				// set cc addresses if any
				if (!item.getCc().isEmpty()) {
					addresses = new Address[item.getCc().size()];
					for (int i = 0; i < item.getCc().size(); i++) {
						addresses[i] = new InternetAddress(item.getCc().get(i));
					}
					message.setRecipients(javax.mail.Message.RecipientType.CC, addresses);
				}
				// set bcc addresses
				if (!item.getBcc().isEmpty()) {
					addresses = new Address[item.getBcc().size()];
					for (int i = 0; i < item.getBcc().size(); i++) {
						addresses[i] = new InternetAddress(item.getBcc().get(i));
					}
					message.setRecipients(javax.mail.Message.RecipientType.BCC, addresses);
				}

				// Add a MIME part to the message for body
				MimeMultipart mp = new MimeMultipart();
				BodyPart part = new MimeBodyPart();
				if (item.isHtml()) {
					part.setContent(item.getBody(), "text/html; charset=utf-8");
				} else {
					part.setText(item.getBody());
				}
				mp.addBodyPart(part);
				// add attachments part of message
				for (AWSAttachment file : item.getFiles()) {
					MimeBodyPart attachment = new MimeBodyPart();
					DataSource ds = new ByteArrayDataSource(file.getContent(), file.getContentType());
					attachment.setDataHandler(new DataHandler(ds));
					attachment.setHeader("Content-ID", "<" + UUID.randomUUID().toString() + ">");
					attachment.setFileName(file.getName());
					mp.addBodyPart(attachment);
				}

				// set message contents
				message.setContent(mp);

				// Send the email.
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				message.writeTo(outputStream);
				RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
				SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
				getEmailClient().sendRawEmail(rawEmailRequest);
				email.setStatus("SUCCESS");
				log.info("Send email successful");
			} catch (Exception e) {
//                        log.error("Cannot send email to ");
				e.printStackTrace();
				//email.setDescription("ERR: " + e.getMessage());
				email.setStatus("FAILED");
			}
			emailRepository.save(email);
		});
	}

	public void sendAsyncSingle(SESFrom sesFrom, Email email) {
		CompletableFuture.runAsync(() -> {
			try {
				Session session = Session.getDefaultInstance(new Properties());
				MimeMessage message = new MimeMessage(session);
				// set subject
				message.setSubject(email.getSubject(), "UTF-8");
				// set message receiversMailLogger
				message.setFrom(new InternetAddress(sesFrom.getEmail(), sesFrom.getName()));
				// set to address
				message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(email.getReceiver()));

				// Add a MIME part to the message for body
				MimeMultipart mp = new MimeMultipart();
				BodyPart part = new MimeBodyPart();
				if (email.isHtml()) {
					part.setContent(email.getContent(), "text/html; charset=utf-8");
				} else {
					part.setText(email.getContent());
				}
				mp.addBodyPart(part);

				// set message contents
				message.setContent(mp);

				// Send the email.
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				message.writeTo(outputStream);
				RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
				SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
				getEmailClient().sendRawEmail(rawEmailRequest);
				email.setStatus("SUCCESS");
				log.info("Send email to {} successful", email.getReceiver());
			} catch (Exception e) {
//                        log.error("Cannot send email to ");
				e.printStackTrace();
				email.setDescription("ERR: " + e.getMessage());
				email.setStatus("FAILED");
			}
			emailRepository.save(email);
		});
	}
}
