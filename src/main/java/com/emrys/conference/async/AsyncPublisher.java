package com.emrys.conference.async;

import com.emrys.conference.model.Account;
import com.emrys.conference.model.Password;
import com.emrys.conference.util.ResetPasswordEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@Component
public class AsyncPublisher {



    @Autowired
    private ApplicationEventPublisher eventPublisher;



    public void publish(Password password, Account account){

        System.out.println("\n\n########################" +System.currentTimeMillis() +"####################################\n\n");

        eventPublisher.publishEvent(new ResetPasswordEvent(password,account));


        System.out.println("\n\n########################" +System.currentTimeMillis() +"####################################\n\n");


    }
}
