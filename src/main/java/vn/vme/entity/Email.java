package vn.vme.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import vn.vme.io.notify.EmailMultiRequest;
import vn.vme.io.notify.EmailMultiWFileRequest;
import vn.vme.io.notify.EmailRequest;
import vn.vme.io.notify.EmailVO;

/**
 * The persistent class for the tenant database table.
 */
@Entity
@Table(schema = "notify_schema", name = "email")
@NamedQuery(name = "Email.findAll", query = "SELECT c FROM Email c")
@Getter
@Setter
public class Email implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver;
    private String cc;
    private String bcc;
    private String sender;

    private String subject;
    private String content;
    private String files;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    private String status;
    private String description;
    private boolean isHtml;

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = new Date();
    }

    public Email() {
    }

    public Email(EmailRequest request) {
        BeanUtils.copyProperties(request, this);
    }

    public Email(EmailMultiRequest request) {
        receiver = Arrays.toString(request.getReceivers());
        if (request.getCc() != null)
            cc = Arrays.toString(request.getCc());
        if (request.getBcc() != null)
            bcc = Arrays.toString(request.getBcc());
        subject = request.getSubject();
        content = request.getContent();
    }

    public Email(EmailMultiWFileRequest request) {
        receiver = Arrays.toString(request.getReceivers());
        if (request.getCc() != null)
            cc = Arrays.toString(request.getCc());
        if (request.getBcc() != null)
            bcc = Arrays.toString(request.getBcc());
        subject = request.getSubject();
        content = request.getContent();
        if (request.getAttachFiles() != null && request.getAttachFiles().length > 0) {
            String fStr = "";
            for (MultipartFile file : request.getAttachFiles()) {
                fStr += file.getOriginalFilename();
            }
            files = fStr;
        }
        sender = request.getSender();
        isHtml = request.getIsHtml().equals("1") || request.getIsHtml().equals("true");
    }

    public EmailVO getVO() {
        EmailVO response = new EmailVO();
        BeanUtils.copyProperties(this, response);
        return response;
    }
    
  

    public String toString() {
        return this.subject + "," + this.sender + "," + this.receiver;
    }
}
