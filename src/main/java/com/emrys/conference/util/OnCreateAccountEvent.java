package com.emrys.conference.util;

import com.emrys.conference.model.Account;
import org.springframework.context.ApplicationEvent;

public class OnCreateAccountEvent extends ApplicationEvent {

    private String appUrl;
    private Account account;

    public OnCreateAccountEvent(Account account, String appUrl) {
        super(account);

        this.account = account;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
