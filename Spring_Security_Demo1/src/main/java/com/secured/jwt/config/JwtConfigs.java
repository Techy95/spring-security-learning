package com.secured.jwt.config;

import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.jwt")
@NoArgsConstructor
public class JwtConfigs {

    private String tokenPrefix;
    private String secretKey;
    private Integer tokenExpirationDays;

    public JwtConfigs(String secretKey, Integer tokenExpirationDays, String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
        this.secretKey = secretKey;
        this.tokenExpirationDays = tokenExpirationDays;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Integer getTokenExpirationDays() {
        return tokenExpirationDays;
    }

    public void setTokenExpirationDays(Integer tokenExpirationDays) {
        this.tokenExpirationDays = tokenExpirationDays;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
