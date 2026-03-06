package archive.backend.global.provider;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private Key key;

    private Dotenv dotenv;

    // 서버 실행 시 init 문자열 키를 암호화 키로 변환
    @PostConstruct
    protected void init() {
        this.dotenv = Dotenv.load();
        byte[] keyBytes = dotenv.get("JWT_SECRET").getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String loginId, String role) {
        // Claims(정보) 설정
        Claims claims = Jwts.claims().setSubject(loginId);
        claims.put("role", role);

        Date now = new Date();
        Date validTime = new Date(now.getTime() + Long.parseLong(dotenv.get("JWT_EXPIRATION")));

        return Jwts.builder()
                .setClaims(claims) // 유저 정보
                .setIssuedAt(now) // 발급 시간
                .setExpiration(validTime) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서버 키값
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            // token 열어보기
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            // 만료시간이 현재 시간보다 뒤에 있으면 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // 아까 담았던 loginId를 반환
    }
}
