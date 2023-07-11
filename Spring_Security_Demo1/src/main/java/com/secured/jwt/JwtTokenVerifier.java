package com.secured.jwt;

import com.secured.jwt.config.JwtConfigs;
import com.secured.jwt.config.JwtSecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenVerifier extends OncePerRequestFilter {

    public JwtTokenVerifier(JwtSecretKey secretKey, JwtConfigs jwtConfiguration) {
        this.secretKey = secretKey;
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader(jwtConfiguration.getAuthorizationHeader());

        if(Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfiguration.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.replace(jwtConfiguration.getTokenPrefix(), "");

        try {
            Jws<Claims> claimsJwt = Jwts.parser()
                    .setSigningKey(secretKey.getSecretKey())
                    .parseClaimsJws(token);

            Claims body = claimsJwt.getBody();
            String userName = body.getSubject();

            Set<SimpleGrantedAuthority> authorities =
                    ((List<Map<String, String>>) body.get("authorities"))
                            .stream()
                            .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                            .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userName,
                    null,
                    authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            log.error("\n\n********************************************\n\nInvalid token value: {}\n\n********************************************", token);
        }

        filterChain.doFilter(request, response);
    }

    private JwtSecretKey secretKey;
    private JwtConfigs jwtConfiguration;
}
