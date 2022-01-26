package com.emrys.conference.repositories;

import com.emrys.conference.model.Account;
import com.emrys.conference.model.ConferenceUserDetails;
import com.emrys.conference.model.VerificationToken;
import com.emrys.conference.service.AccountService;

public interface AccountRepository {

    Account create(Account account);

    void saveToken(VerificationToken verificationToken);

    VerificationToken findByToken(String token);

    Account findAccountByUsername(String username);

    void createUserDetails(ConferenceUserDetails userDetails);

    void createAuthorities(ConferenceUserDetails userDetails);

    void deleteAccount(Account account);

    void deleteToken(Account account);
}
