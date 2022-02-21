package com.example.UserManagementSystem.AppUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor @Slf4j
public class AppUserService {

    private final AppUserRepo repo;

    public List<AppUser> getUsers(){
        log.info("retrieving users...");
        return repo.findAll();
    }

    public void addUser(AppUser user){
        user.setUserID(UUID.randomUUID().toString());
        user.setCreatedDate(LocalDateTime.now());
        repo.save(user);
        log.info("user saved :)");
    }

    public void updateUser(AppUser user){
        repo.save(user);
    }

    public void deleteUser(Long id){
        repo.deleteById(id);
        log.info("deleted user with id: " + id);
    }
}
