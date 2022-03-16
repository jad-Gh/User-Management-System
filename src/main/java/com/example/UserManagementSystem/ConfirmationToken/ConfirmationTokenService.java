package com.example.UserManagementSystem.ConfirmationToken;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor @Slf4j
public class ConfirmationTokenService {

    private ConfirmationTokenRepo repo;

    public void addConfirmationToken(ConfirmationToken confirmationToken){
        repo.save(confirmationToken);
    }

    public ConfirmationToken findToken(String token){
        ConfirmationToken tokenFound = repo.findByToken(token).orElseThrow(()->new IllegalStateException("Token not Found"));
        return tokenFound;
    }

    public void deleteToken(Long id){
        repo.deleteById(id);
    }

}
