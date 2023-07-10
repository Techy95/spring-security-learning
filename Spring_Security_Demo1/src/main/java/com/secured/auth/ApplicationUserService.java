package com.secured.auth;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@NoArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return this.userDao.selectApplicationUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("No data found with given user name: {}", userName)));
    }

    @Autowired
    public ApplicationUserService(@Qualifier("fake") ApplicationUserDAO userDao) {
        this.userDao = userDao;
    }

    private ApplicationUserDAO userDao;
}
