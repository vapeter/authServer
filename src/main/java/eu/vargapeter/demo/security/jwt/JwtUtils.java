package eu.vargapeter.demo.security.jwt;

import eu.vargapeter.demo.security.MyEmployeeDetails;
import eu.vargapeter.demo.security.MyUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

  private String jwtSecret = "04ca023b39512e46d0c2cf4b48d5aac61d34302994c87ed4eff225dcf3b0a218739f3897051a057f9b846a69ea2927a587044164b7bae5e1306219d50b588cb1";

  private int jwtExpirationMs = 86400000;

  public String generateJwtToken(Authentication authentication) {

    if ( authentication.getPrincipal() instanceof MyUserDetails) {
      MyUserDetails userPrincipal = (MyUserDetails) authentication.getPrincipal();
      return Jwts.builder()
              .setSubject((userPrincipal.getEmail()))
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
              .signWith(SignatureAlgorithm.HS512, jwtSecret)
              .compact();
    } else {
      MyEmployeeDetails userPrincipal = (MyEmployeeDetails) authentication.getPrincipal();
      return Jwts.builder()
              .setSubject((userPrincipal.getEmail()))
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
              .signWith(SignatureAlgorithm.HS512, jwtSecret)
              .compact();
    }

  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
