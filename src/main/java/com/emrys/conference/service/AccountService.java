package com.emrys.conference.service;

import com.emrys.conference.model.Account;
import com.emrys.conference.model.Password;
import com.emrys.conference.model.VerificationToken;

public interface AccountService {

     Account create(Account account);

    void createVerificationToken(Account account, String token);

    void confirmAccount(String token);

     Account getAccountByUsennameAndEmail(String username, String email);

     VerificationToken confirmPasswordResetToken(String token);

    void updateAccount(Password password);
}
