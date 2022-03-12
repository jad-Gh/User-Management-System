package com.example.UserManagementSystem.Security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.UserManagementSystem.AppUser.AppUser;
import com.example.UserManagementSystem.AppUser.AppUserRepo;
import com.example.UserManagementSystem.Response.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(value = "*",maxAge = 3600)
@Slf4j
@AllArgsConstructor
public class AuthController {
    @Autowired
    private final AuthenticationManager authenticationManager;

    private final AppUserRepo repo;

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
                .withClaim("Type","Access Token")
                .withClaim("Roles", userDetailsImp.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(userDetailsImp.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 100 * 60 * 1000))
                .withClaim("Type","Refresh Token")
                .withClaim("Roles", userDetailsImp.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .message("Success")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(Map.of("access_token",access_token,"refresh_token",refresh_token)).build()
        );

    }

    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshUserToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decoded = verifier.verify(token);
            String userName = decoded.getSubject();
            AppUser user = repo.findByEmail(userName).orElseThrow();
            String access_token = JWT.create()
                    .withSubject(user.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                    .withClaim("Type","Access Token")
                    .withClaim("Roles", List.of(user.getRole().getRoleName()))
                    .sign(algorithm);

            return ResponseEntity.ok(Response.builder()
                    .timestamp(LocalDateTime.now())
                    .message("Success")
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK)
                    .data(Map.of("access_token",access_token)).build()
            );


        } else {
            throw new RuntimeException("Refresh Token is missing");
        }
    }

}

@Data
class EmailAndPass{
    private String email;
    private String password;
}
