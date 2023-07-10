package com.secured.auth;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserDAO /*implements JPARepository<String, ApplicationUser>*/ {

//    public List<ApplicationUser>;
    Optional<ApplicationUser> selectApplicationUserByUserName(String userName);
}
