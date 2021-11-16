package vn.vme.ses;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import vn.vme.service.EmailService;

public class SESWorker extends Thread {

    private static final Logger LOG = Logger.getLogger(SESWorker.class.getName());

    private boolean iCanContinue = true;

    private final Queue<AWSEmail> queue = new LinkedList<>();

    private AWSEmail item;

    private final int MAX_SLEEP_TIME = 3 * 60 * 60 * 1000; //3 hours

    private AmazonSimpleEmailService sesClient;

    private final int id;

    private EmailService emailService;

    public SESWorker(int id, EmailService emailService) {
        super("SESWorker-" + id);
        setDaemon(true);
        this.id = id;
        this.emailService = emailService;
    }

    @Override
    public void run() {
        try {
//            AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
            sesClient = emailService.getEmailClient();
        } catch (Exception ex) {
            Logger.getLogger(SESWorker.class.getName()).log(Level.SEVERE, null, ex);
        }

        LOG.log(Level.INFO, "Amazon SES worker-{0} is up and running.", id);

        /*while (iCanContinue) {
            synchronized (queue) {
                // Check for a new item from the queue
                if (queue.isEmpty()) {
                    // Sleep for it, if there is nothing to do
                    LOG.log(Level.INFO, "Waiting for Amazon SES to send...{0}", getTime());
                    try {
                        queue.wait(MAX_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        LOG.log(Level.INFO, "Interrupted...{0}", getTime());
                    }
                }
                // Take new item from the top of the queue
                item = queue.poll();
                // Null if queue is empty
                if (item == null) {
                    continue;
                }
                try {
//                     send simple formatted message

                    if (item.getFiles().isEmpty()) {
                        // Construct an object to contain the recipient address.
                        Destination destination = new Destination().withToAddresses(item.getTo());
                        //set cc & bcc addresses
                        if (item.getCc().size() > 0) {
                            destination.withCcAddresses(item.getCc());
                        }
                        if (item.getBcc().size() > 0) {
                            destination.withBccAddresses(item.getBcc());
                        }
                        // Create the subject and body of the message.
                        Content subject = new Content().withData(item.getSubject());
                        Content textBody = new Content().withData(item.getBody());
                        Body body = item.isHtml() ? new Body().withHtml(textBody) : new Body().withText(textBody);

                        // Create a message with the specified subject and body.
                        Message message = new Message().withSubject(subject).withBody(body);
                        // Assemble the email.
                        SendEmailRequest request = new SendEmailRequest().withSource(item.getFrom().getEmail())
                                .withReplyToAddresses(item.getFrom().getEmail())
                                .withDestination(destination)
                                .withMessage(message);
                        // Send the email.
                        sesClient.sendEmail(request);
                    }
//                     send raw message with attachment

                    else {
                        Session session = Session.getDefaultInstance(new Properties());
                        MimeMessage message = new MimeMessage(session);
                        //set subject
                        message.setSubject(item.getSubject(), "UTF-8");
                        //set message receiversMailLogger
                        message.setFrom(new InternetAddress(SESFrom.NO_REPLY.getEmail()));
                        message.setReplyTo(new Address[]{new InternetAddress(SESFrom.NO_REPLY.getEmail())});
                        //set to address
                        Address[] addresses = new Address[item.getTo().size()];
                        for (int i = 0; i < item.getTo().size(); i++) {
                            addresses[i] = new InternetAddress(item.getTo().get(i));
                        }
                        message.setRecipients(javax.mail.Message.RecipientType.TO, addresses);
                        //set cc addresses if any
                        if (!item.getCc().isEmpty()) {
                            addresses = new Address[item.getCc().size()];
                            for (int i = 0; i < item.getCc().size(); i++) {
                                addresses[i] = new InternetAddress(item.getCc().get(i));
                            }
                            message.setRecipients(javax.mail.Message.RecipientType.CC, addresses);
                        }
                        //set bcc addresses
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
                            part.setContent(item.getBody(), "text/html");
                        } else {
                            part.setText(item.getBody());
                        }
                        mp.addBodyPart(part);
                        //add attachments part of message
                        for (AWSAttachment file : item.getFiles()) {
                            MimeBodyPart attachment = new MimeBodyPart();
                            DataSource ds = new ByteArrayDataSource(file.getContent(), file.getContentType());
                            attachment.setDataHandler(new DataHandler(ds));
                            attachment.setHeader("Content-ID", "<" + UUID.randomUUID().toString() + ">");
                            attachment.setFileName(file.getName());
                            mp.addBodyPart(attachment);
                        }

                        //set message contents
                        message.setContent(mp);

                        // Send the email.
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        message.writeTo(outputStream);
                        RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

                        SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
                        sesClient.sendRawEmail(rawEmailRequest);
                    }
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Exception while sending SES email ...{0}",
                            ex.getMessage());
                }
            }
        }*/
    }

    public void add(AWSEmail item) {
        synchronized (queue) {
            queue.add(item);
            queue.notify();
            LOG.log(Level.INFO, "New Amazon SES email added into queue for  worker-{0}...", id);
        }
    }

    public void stopWorker() {
        LOG.log(Level.INFO, "Stopping Amazon SES worker-{0}...", id);
        try {
            iCanContinue = false;
            this.interrupt();
            this.join();
        } catch (InterruptedException | NullPointerException e) {
            LOG.log(Level.SEVERE, "Exception while stopping Amazon SES worker...{0}",
                    e.getMessage());
        }
    }

//     * Get current server date & time

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        return format.format(new Date());
    }
}
