package com.emrys.conference.controller;

import com.emrys.conference.model.Account;
import com.emrys.conference.service.AccountService;
import com.emrys.conference.util.OnCreateAccountEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("account")
    public String getRegistration(@ModelAttribute("account")Account account){
        return "account";
    }

    @PostMapping("account")
    private String addRegistration(@ModelAttribute("account") Account account, BindingResult result){

//        check for error

//        should verify that the account and the user don't already exist'

//        should verify valid email address

//        encrypt password
          account.setPassword(passwordEncoder.encode(account.getPassword()));

//        create account
        account = accountService.create(account);

//        fire off an event on creation
        eventPublisher.publishEvent(new OnCreateAccountEvent(account,"conference"));

        return "redirect:account";
    }

    @GetMapping("accountConfirm")
    public String confirmAccount(@RequestParam("token") String token){
        accountService.confirmAccount(token);

        return "accountConfirmed";
    }
}
