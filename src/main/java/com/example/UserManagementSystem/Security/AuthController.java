package com.example.UserManagementSystem.Security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(value = "*",maxAge = 3600)
@Slf4j
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody EmailAndPass emailAndPass){
        log.info("Username is {}, Password is {}",emailAndPass.getEmail(),emailAndPass.getPassword());
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(emailAndPass.getEmail(),emailAndPass.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImp userDetailsImp = (UserDetailsImp) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(userDetailsImp.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles", userDetailsImp.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return ResponseEntity.ok().body(access_token);
    }

}

@Data
class EmailAndPass{
    private String email;
    private String password;
}
