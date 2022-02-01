package com.emrys.conference.util;

import com.emrys.conference.model.Account;
import com.emrys.conference.model.Password;
import org.springframework.context.ApplicationEvent;

public class ResetPasswordEvent extends ApplicationEvent {
    private Password password;
    private Account account;

    public ResetPasswordEvent(Password password,Account account ) {
        super(password);

        this.password = password;
        this.account= account;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
