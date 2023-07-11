package com.secured.jwt.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtSecretKey {

    @Autowired
    public JwtSecretKey(JwtConfigs jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    @Bean
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfiguration.getSecretKey().getBytes());
    }

    private final JwtConfigs jwtConfiguration;
}
