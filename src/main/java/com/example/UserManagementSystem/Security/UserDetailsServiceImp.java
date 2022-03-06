package com.example.UserManagementSystem.Security;

import com.example.UserManagementSystem.AppUser.AppUser;
import com.example.UserManagementSystem.AppUser.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    private final AppUserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = repo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User with email, "+email+", was not found"));
        return new UserDetailsImp(user);
    }
}
