package zerobase.stockdividend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zerobase.stockdividend.service.MemberService;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final String KEY_ROLES = "roles";
    private final long EXPIRATION = 1000 * 60 * 60 * 5; // 5시간
    private final MemberService memberService;

    @Value(value = "${spring.jwt.secret}")
    private String secretKey;

//    private Key getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     *
     *  토큰 생성
     */
    public String generateToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().add(KEY_ROLES, roles).subject(username).build();

        var now = new Date();
        var expirationDate = new Date(now.getTime() + EXPIRATION);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(this.getSigningKey())
                .compact();
    }

    /**
     * 토큰의 유효성 확인
     */

    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails
                = this.memberService.loadUserByUsername(this.getUserName(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails,
                "", userDetails.getAuthorities());
    }

    //token에 username 맞는지 확인
    public String getUserName(String token) {
        return this.parseClaims(token).getSubject();
    }

    //토큰 기간 만료 확인
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
