package com.creasy.email.service.impl;

import com.creasy.pojo.Email;
import com.creasy.email.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements IEmailService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JavaMailSenderImpl sender;

    @Override
    public boolean sendEmail(Email email) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent());
            helper.setFrom(email.getFrom());
            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
