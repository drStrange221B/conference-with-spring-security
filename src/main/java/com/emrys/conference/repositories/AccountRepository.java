package com.emrys.conference.repositories;

import com.emrys.conference.model.Account;
import com.emrys.conference.model.ConferenceUserDetails;
import com.emrys.conference.model.Password;
import com.emrys.conference.model.VerificationToken;

public interface AccountRepository {

    Account getAccountByUsernameAndEmail(String username, String email);

    Account create(Account account);

    void saveToken(VerificationToken verificationToken);

    VerificationToken findByToken(String token);

    Account findAccountByUsername(String username);

    void createUserDetails(ConferenceUserDetails userDetails);

    void createAuthorities(ConferenceUserDetails userDetails);

    void deleteAccount(Account account);

    void deleteToken(String token);

    void updateAccount(Password password);

    void updateUserDetails(ConferenceUserDetails user);
}
