package com.example.UserManagementSystem.EmailSender;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
public class EmailSender {

    private final JavaMailSender mailSender;

    @Async
    public void send(String to,String email){
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(email,true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom("some.email@hotmail.com");
            mimeMessageHelper.setSubject("Confirm your Email!");
            mailSender.send(mimeMessage);

        }catch(MessagingException e){
            log.error("Error Sending Email: ", e.getMessage());
            throw new IllegalStateException("Error Sending Email");
        }
    }

}
