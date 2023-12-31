package com.secured.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import static com.secured.security.ApplicationUserPermission.COURSE_READ;
import static com.secured.security.ApplicationUserPermission.COURSE_WRITE;
import static com.secured.security.ApplicationUserPermission.STUDENT_READ;
import static com.secured.security.ApplicationUserPermission.STUDENT_WRITE;
import static com.secured.security.ApplicationUserRole.ADMIN;
import static com.secured.security.ApplicationUserRole.ADMIN_TRAINEE;
import static com.secured.security.ApplicationUserRole.STUDENT;

@Component
@EnableWebSecurity
@AllArgsConstructor
public class ApplicationConfigSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         *  antMatchers order is important,
         *  If compiler finds any matching in the first come-first serve basis,
         *  it exists further antMatching statements
         */
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/css/*", "/js/*").permitAll()    //  Permit mentioned URIs to -  all
                .antMatchers("/api/**").hasRole(STUDENT.name())                          //      - students
                .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())  //  ROLE AWARE Application                      //      - students
                .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())                          //      - students
                .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())                          //      - students
                .antMatchers(HttpMethod.PATCH,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())                          //      - students
                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(), ADMIN_TRAINEE.name())                          //      - students
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        UserDetails abc = User.builder()
                .username("qwe")
                .password(encoder.encode("asd"))
//                .roles(STUDENT.name())   //  ROLE_STUDENT
                .authorities(STUDENT.getGrantedAuthorities())
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("asdf"))
//                .roles(ADMIN.name())   //  ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();
        UserDetails adminTrainee = User.builder()
                .username("admin2")
                .password(encoder.encode("asdf"))
//                .roles(ADMIN_TRAINEE.name())    //  ROLE_ADMIN_TRAINEE
                .authorities(ADMIN_TRAINEE.getGrantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(abc,
                admin,
                adminTrainee);
    }

    private final PasswordEncoder encoder;
}
