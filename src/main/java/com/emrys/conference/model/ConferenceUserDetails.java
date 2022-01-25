package com.emrys.conference.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ConferenceUserDetails extends org.springframework.security.core.userdetails.User{

    private String nickName;

    public ConferenceUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
