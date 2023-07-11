package com.secured.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secured.jwt.config.JwtConfigs;
import com.secured.jwt.config.JwtSecretKey;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JWTUserNameAndPasswordAuthFilter extends UsernamePasswordAuthenticationFilter {
    /**
     *
     * @param request
     * @param response
     * @return  The first step of JWT - receive and then authenticate the credentials
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UserNameAndPasswordAuthRequest authRequest =
                    new ObjectMapper().readValue(
                            request.getInputStream(), UserNameAndPasswordAuthRequest.class);

            Authentication authentication = authenticationManager.authenticate(
                                    new UsernamePasswordAuthenticationToken(
                                            authRequest.getUserName(),
                                            authRequest.getPassword()));

            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain, Authentication authResult) throws IOException, ServletException {
        LocalDate expirationDate = LocalDate.now().plusDays(jwtConfiguration.getTokenExpirationDays());
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(expirationDate))
                .signWith(secretKey.getSecretKey())
                .compact();
        response.addHeader(jwtConfiguration.getAuthorizationHeader(), jwtConfiguration.getTokenPrefix() + token);
    }

    public JWTUserNameAndPasswordAuthFilter(AuthenticationManager authenticationManager, JwtSecretKey secretKey,
                                            JwtConfigs jwtConfiguration) {
        this.authenticationManager = authenticationManager;
        this.secretKey = secretKey;
        this.jwtConfiguration = jwtConfiguration;
    }

    private final AuthenticationManager authenticationManager;
    private final JwtSecretKey secretKey;
    private final JwtConfigs jwtConfiguration;
}
