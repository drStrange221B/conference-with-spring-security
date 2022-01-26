package com.emrys.conference.repositories;

import com.emrys.conference.model.Account;
import com.emrys.conference.model.ConferenceUserDetails;
import com.emrys.conference.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

@Repository
public class AccountRepositoryImpl implements AccountRepository{

    private final String CREATE_ACCOUNT= "INSERT INTO accounts (username, firstname, lastname, password, email) " +
            "values(?,?,?,?,?)";

    private final String INSERT_TOKEN = "INSERT INTO verification_tokens (username, token, expiry_date) " +
            "VALUES (?, ?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account create(Account account) {

        jdbcTemplate.update(CREATE_ACCOUNT, account.getUsername(), account.getFirstName(),
                account.getLastName(), account.getPassword(), account.getEmail());

        return account;
    }

    @Override
    public void saveToken(VerificationToken verificationToken) {
        jdbcTemplate.update(INSERT_TOKEN,verificationToken.getUsername(),verificationToken.getToken(),
                verificationToken.calculateExpiryDate(VerificationToken.EXPIRATION));


    }

    @Override
    public VerificationToken findByToken(String token) {
      VerificationToken verificationToken =  jdbcTemplate.queryForObject("select username, token, expiry_date from verification_tokens where" +
                " token=?", new RowMapper<VerificationToken>(){

            @Override
            public VerificationToken mapRow(ResultSet resultSet, int i) throws SQLException {
                VerificationToken verificationToken1= new VerificationToken();
                verificationToken1.setToken(resultSet.getString("token"));
                verificationToken1.setUsername(resultSet.getString("username"));
                verificationToken1.setExpiryDate(resultSet.getTimestamp("expiry_date"));
                return verificationToken1;
            }
        },token);
        return verificationToken;
    }

    @Override
    public Account findAccountByUsername(String username) {
        Account account = jdbcTemplate.queryForObject("select username, firstname, lastname, password,email from accounts" +
                " where username=?", new RowMapper<Account>() {
            @Override
            public Account mapRow(ResultSet resultSet, int i) throws SQLException {
                Account account1 = new Account();
                account1.setUsername(resultSet.getString("username"));
                account1.setFirstName(resultSet.getString("firstname"));
                account1.setLastName(resultSet.getString("lastname"));
                account1.setPassword(resultSet.getString("password"));
                account1.setEmail(resultSet.getString("email"));
                return account1;
            }
        },username);
        return account;
    }

    @Override
    public void createUserDetails(ConferenceUserDetails userDetails) {

        jdbcTemplate.update("INSERT INTO users (username, password, enabled) values " +
                "(?, ?, ?)", userDetails.getUsername(), userDetails.getPassword(),1);
    }

    @Override
    public void createAuthorities(ConferenceUserDetails userDetails) {

        Iterator<GrantedAuthority> itr = userDetails.getAuthorities().iterator();

        while(itr.hasNext()) {
            jdbcTemplate.update("INSERT INTO authorities(username, authority) values (?, ?)"
                    ,userDetails.getUsername(),itr.next().getAuthority());
        }
    }

    @Override
    public void deleteAccount(Account account) {
        jdbcTemplate.update("DELETE from accounts where username=?",account.getUsername());
    }

    @Override
    public void deleteToken(Account account) {
        jdbcTemplate.update("DELETE from verification_tokens where username=?", account.getUsername());
    }
}
