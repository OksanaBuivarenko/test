package com.fintech.database.security.jwt;

import com.fintech.database.entity.Token;
import com.fintech.database.repository.TokenRepository;
import com.fintech.database.security.AppUserDetails;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@EnableScheduling
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.tokenExpirationDefault}")
    private Duration tokenExpirationDefault;

    @Value("${app.jwt.rememberTokenExpiration}")
    private Duration rememberTokenExpiration;

    private final TokenRepository tokenRepository;

    public String generateJwtToken(AppUserDetails userDetails, Boolean remember) {
        String jwt = generateTokenFromUsername(userDetails.getUsername(), remember);
        Token token = new Token();
        token.setJwt(jwt);
        token.setUserId(userDetails.getId());
        tokenRepository.save(token);
        return jwt;
    }

    public String generateTokenFromUsername(String username, Boolean remember) {
        Duration tokenExpiration = (remember) ? rememberTokenExpiration : tokenExpirationDefault;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsername(String jwt) {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(jwt).getBody().getSubject();
    }

    public boolean validate(String jwt) {
        try {
            Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(jwt);
            if (isTokenContainsFromDB(jwt)) {
                return true;
            } else {
                throw new MalformedJwtException("Token not found in storage");
            }
        } catch (SignatureException e) {
            log.error("Invalid signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Boolean isTokenContainsFromDB(String jwt) {
        return tokenRepository.existsByJwt(jwt);
    }

    public void deleteTokenFromDb(Long userId) {
        Token token = tokenRepository.findByUserId(userId);
        tokenRepository.delete(token);
    }

    @Scheduled(cron = "${scheduler.tokens-clear}")
    public void deleteIsExpiredTokensFromDb() {
        List<Token> tokens = tokenRepository.findAll();
        for (Token token : tokens) {
            try {
                Jwts.parser().setSigningKey(secret)
                        .parseClaimsJws(token.getJwt()).getBody().getExpiration();
            } catch (ExpiredJwtException e) {
                tokenRepository.delete(token);
                log.info("Token delete by cron");
            }
        }
    }
}