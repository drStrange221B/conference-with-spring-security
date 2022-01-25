package com.emrys.conference.securityconfig;

import com.emrys.conference.model.ConferenceUserDetails;
import com.emrys.conference.service.ConferenceUserDetailsContextMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ConferenceUserDetailsContextMapper ctxMapper;

    @Autowired
    private DataSource datasource;

    @Override
    protected void configure(final HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/anonymous*").anonymous()
                .antMatchers("/login**").permitAll()
                .antMatchers("/assets/css/**", "assets/js/**", "/images/**").permitAll()
                .antMatchers("/index*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/perform")
                .defaultSuccessUrl("/", true);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception{

        auth.ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=emrys,dc=com")
                .and()
                .passwordCompare()
                .passwordEncoder(passwordEncoder())
                .passwordAttribute("userPassword")
                .and()
                .userDetailsContextMapper(ctxMapper);
        ;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
