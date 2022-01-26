package com.emrys.conference.service;

import com.emrys.conference.model.Account;
import com.emrys.conference.model.ConferenceUserDetails;
import com.emrys.conference.model.VerificationToken;
import com.emrys.conference.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account create(Account account) {
        return accountRepository.create(account);
    }

    @Override
    public void createVerificationToken(Account account, String token) {

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsername(account.getUsername());

        accountRepository.saveToken(verificationToken);

    }

    @Override
    public void confirmAccount(String token) {

        // retrieve token
        VerificationToken verificationToken = accountRepository.findByToken(token);

        // verify date
        if(verificationToken.getExpiryDate().after(new Date())) {

            // move from account table to userdetails table
            Account account = accountRepository.findAccountByUsername(verificationToken.getUsername());

            // create user details
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            ConferenceUserDetails userDetails = new ConferenceUserDetails(account.getUsername(), account.getPassword(), authorities);

            accountRepository.createUserDetails(userDetails);
            accountRepository.createAuthorities(userDetails);

            //delete from accounts
            accountRepository.deleteAccount(account);
            accountRepository.deleteToken(account);

            // delete from tokens
        }

    }
}
