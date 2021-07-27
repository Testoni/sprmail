package com.example.sprmail.services;

import com.example.sprmail.enums.Status;
import com.example.sprmail.models.Email;
import com.example.sprmail.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private JavaMailSender emailSender;

    public Email sendEmail(Email emailModel) {
        emailModel.setSendDateEmail(LocalDateTime.now());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            emailSender.send(message);

            emailModel.setStatus(Status.SENT);
        } catch (MailException e) {
            emailModel.setStatus(Status.ERORR);
        } finally {
            return emailRepository.save(emailModel);
        }
    }

    public Page<Email> findAll(Pageable pageable) {
        return emailRepository.findAll(pageable);
    }
}
