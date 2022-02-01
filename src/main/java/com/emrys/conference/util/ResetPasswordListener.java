package com.emrys.conference.util;

import com.emrys.conference.model.Account;
import com.emrys.conference.model.Password;
import com.emrys.conference.model.VerificationToken;
import com.emrys.conference.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Async
public class ResetPasswordListener implements ApplicationListener<ResetPasswordEvent> {

    @Value("${serverUrl}")
    private String serverUrl;

    @Value("${appUrl}")
    private String appUrl;
    @Autowired
    AccountService accountService;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(ResetPasswordEvent resetPasswordEvent) {
        this.sendRestEmail(resetPasswordEvent);
    }

    @Async
    private void sendRestEmail(ResetPasswordEvent resetPasswordEvent) {


        System.out.printf("Inside event listener!");
        Account account = resetPasswordEvent.getAccount();
        String token = UUID.randomUUID().toString();
        accountService.createVerificationToken(account, token);

        String destinationEmail = account.getEmail();
        String subject= "Password Reset";
        String message = "Please click here to reset password: \t\n " + serverUrl + appUrl + "resetPassword?token=" + token;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setTo(destinationEmail);
        simpleMailMessage.setText(message);

        mailSender.send(simpleMailMessage);
        System.out.println("Email was send !");

    }
}
