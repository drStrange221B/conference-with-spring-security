package com.emrys.conference.controller;

import com.emrys.conference.async.AsyncPublisher;
import com.emrys.conference.model.Account;
import com.emrys.conference.model.Password;
import com.emrys.conference.model.VerificationToken;
import com.emrys.conference.service.AccountService;
import com.emrys.conference.util.ResetPasswordEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class PasswordController {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private AsyncPublisher asyncPublisher;

    @GetMapping("password")
    public String getPasswordReset(@ModelAttribute("password")Password password){

        return "password";
    }
    
    @PostMapping("password")
    @Async
    public String sendEmailToReset(@Valid @ModelAttribute("password") Password password,
                                   BindingResult result){
        // check for errors
        // Get the account with email and username
     Account account =  accountService.getAccountByUsennameAndEmail( password.getUsername(), password.getEmail());

     if(account !=null){

         System.out.println("\n\n########################" +System.currentTimeMillis() +"####################################\n\n");
         eventPublisher.publishEvent(new ResetPasswordEvent(password,account));
         System.out.println("\n\n########################" +System.currentTimeMillis() +"####################################\n\n");

//         asyncPublisher.publish(password, account);
     }

     return "redirect:password";
    }

    @GetMapping("resetPassword")
    public ModelAndView getPassword(@RequestParam("token") String token){

        //verify token
        Password password = new Password();
        password.setToken(token);

        return new ModelAndView("passwordReset","password",password);
    }

    @PostMapping("resetPassword")
    public String addNewPassword(@ModelAttribute("password") Password password){

        VerificationToken verificationToken = accountService.confirmPasswordResetToken(password.getToken());

        if(verificationToken.getExpiryDate().after(new Date())) {
            password.setUsername(verificationToken.getUsername());
            accountService.updateAccount(password);

            return "redirect:passwordReset?reset=true&token=0";
        }else
        {
            return "tokenExpired";
        }

    }
}
