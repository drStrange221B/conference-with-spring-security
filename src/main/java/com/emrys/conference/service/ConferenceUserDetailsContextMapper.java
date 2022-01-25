package com.emrys.conference.service;

import com.emrys.conference.model.ConferenceUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

@Service
public class ConferenceUserDetailsContextMapper implements UserDetailsContextMapper {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String LOAD_USER_BY_USER_NAME =
            "select username, password, enabled, nickname from users where username=?";

    @Override
    public UserDetails mapUserFromContext(DirContextOperations dirContextOperations, String s, Collection<? extends GrantedAuthority> collection) {
        final ConferenceUserDetails userDetails = new ConferenceUserDetails(dirContextOperations.getStringAttribute("uid"),
                "fake", Collections.EMPTY_LIST);


        RowMapper<ConferenceUserDetails> nickname = new RowMapper<>() {


            @Override
            public ConferenceUserDetails mapRow(ResultSet resultSet, int i) throws SQLException {
                userDetails.setNickName(resultSet.getString("nickname"));
                return userDetails;
            }
        };

        jdbcTemplate.queryForObject(LOAD_USER_BY_USER_NAME, nickname
        ,dirContextOperations.getStringAttribute("uid"));

        return userDetails;
    }

    @Override
    public void mapUserToContext(UserDetails userDetails, DirContextAdapter dirContextAdapter) {

    }
}
