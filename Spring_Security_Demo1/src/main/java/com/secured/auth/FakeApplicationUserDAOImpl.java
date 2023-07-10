package com.secured.auth;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

import static com.secured.security.ApplicationUserRole.*;

@Repository("fake")
@NoArgsConstructor
public class FakeApplicationUserDAOImpl implements ApplicationUserDAO {

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUserName(String userName) {
        return getApplicationUsers()
                .stream()
                .filter(user -> user.getUsername().equals(userName))
                .findFirst();
    }

    private Set<ApplicationUser> getApplicationUsers() {
        Set applicationUsers = Sets.newHashSet(
                new ApplicationUser( "qwe", encoder.encode("asd"), STUDENT.getGrantedAuthorities(), true, true, true, true),
                new ApplicationUser( "admin", encoder.encode("asdf"), ADMIN.getGrantedAuthorities(), true, true, true, true),
                new ApplicationUser( "admin2", encoder.encode("asdf"), ADMIN_TRAINEE.getGrantedAuthorities(), true, true, true, true)
        );
        return applicationUsers;
    }

    @Autowired
    public FakeApplicationUserDAOImpl(PasswordEncoder encoder) {
        this.encoder=encoder;
    }

    private PasswordEncoder encoder;
}
