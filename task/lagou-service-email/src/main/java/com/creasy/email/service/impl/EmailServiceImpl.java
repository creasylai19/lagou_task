package com.creasy.email.service.impl;

import com.creasy.email.IEmailService;
import com.creasy.pojo.Email;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailServiceImpl implements IEmailService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JavaMailSenderImpl sender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public boolean sendEmail(Email email) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent());
            helper.setFrom(emailFrom);
            sender.send(message);
            log.debug("my message is : ", message.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
