package com.secured.security;

import com.secured.auth.ApplicationUserService;
import com.secured.auth.JwtTokenVerifier;
import com.secured.jwt.JWTUserNameAndPasswordAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.secured.security.ApplicationUserPermission.COURSE_READ;
import static com.secured.security.ApplicationUserPermission.COURSE_WRITE;
import static com.secured.security.ApplicationUserPermission.STUDENT_READ;
import static com.secured.security.ApplicationUserPermission.STUDENT_WRITE;
import static com.secured.security.ApplicationUserRole.ADMIN;
import static com.secured.security.ApplicationUserRole.ADMIN_TRAINEE;
import static com.secured.security.ApplicationUserRole.STUDENT;

@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //  For Enabling Pre Authorization Of Methods In Global Level
public class ApplicationConfigSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         *  antMatchers order is important,
         *  If compiler finds any matching in the first come-first serve basis,
         *  it exists further antMatching statements
         */
        http
                .csrf().disable() //  Disable csrf
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                /**
                 *  In the above line, Cookies will be inaccessible to client side script
                 *  (even for front end javascript component
                 *
                 *  In below 3 lines, we have added the Stateless session management and JWT based authentication filter
                 */
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTUserNameAndPasswordAuthFilter(authenticationManager()))
                .addFilterAfter(new JwtTokenVerifier(), JWTUserNameAndPasswordAuthFilter.class)
                .authorizeRequests()
                .antMatchers("/", "/css/*", "/js/*").permitAll()    //  Permit mentioned URIs to -  all
                .antMatchers("/api/**").hasRole(STUDENT.name())                          //      - students
                /**
                 * Commenting below authority based checks as it is already present in method level for respective controller
                 */
/*                .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())  //  ROLE AWARE Application                      //      - students
                .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())                          //      - students
                .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())                          //      - students
                .antMatchers(HttpMethod.PATCH,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())                          //      - students
                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(), ADMIN_TRAINEE.name()) */                         //      - students
                .anyRequest()
                .authenticated();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    public ApplicationConfigSecurity(PasswordEncoder encoder, ApplicationUserService userService) {
        this.encoder=encoder;
        this.userService=userService;
    }

    private final PasswordEncoder encoder;
    private final ApplicationUserService userService;
}
