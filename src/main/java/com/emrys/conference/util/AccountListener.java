package com.emrys.conference.util;

import com.emrys.conference.model.Account;
import com.emrys.conference.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountListener implements ApplicationListener<OnCreateAccountEvent> {

    private String serverUrl = "http://localhost:8080/";

    @Autowired
    private AccountService accountService;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnCreateAccountEvent event) {
        this.confirmCreateAccount(event);
    }

    private void confirmCreateAccount(OnCreateAccountEvent event) {

        // get the account
        // create verification token
        Account account = event.getAccount();
        String token = UUID.randomUUID().toString();
        accountService.createVerificationToken(account, token);

        // get email properties
        String destinationEmail = account.getEmail();
        String subject = "Account confirmation";
        String confirmationUrl = event.getAppUrl() + "/accountConfirm?token=" + token;
        String message = "Please Confirm: ";

        // send email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destinationEmail);
        email.setSubject(subject);
        email.setText(message + serverUrl + confirmationUrl);

        mailSender.send(email);

    }
}
