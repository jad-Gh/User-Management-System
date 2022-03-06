package com.example.UserManagementSystem.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j @Component
public class JWTUtil {
    private final String secret = "secret";
    private Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

    public String generateJWTToken(UserDetailsImp user,String tokenName, int expiration){
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .withClaim("Roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public boolean validateJWTToken(String token){
        try{
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decoded = verifier.verify(token);
            return true;
        }catch(Exception e) {
            log.error("Error Validating JWT Token: " + e.getMessage());
            return false;
        }
    }
}
