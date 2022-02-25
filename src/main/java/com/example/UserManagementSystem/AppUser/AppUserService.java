package com.example.UserManagementSystem.AppUser;

import com.example.UserManagementSystem.Roles.AppRole;
import com.example.UserManagementSystem.Roles.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor @Slf4j
public class AppUserService {

    private final AppUserRepo repo;
    private final RoleRepository roleRepo;

    public List<AppUser> getUsers(){
        log.info("retrieving users...");
        return repo.findAll();
    }

    public List<AppRole> getRoles(){
        return roleRepo.findAll();
    }

    public void addUser(AppUser user){
        try{
            user.setUserID(UUID.randomUUID().toString());
            user.setCreatedDate(LocalDateTime.now());
            repo.save(user);
            log.info("user saved");
        }catch(Exception e){
            log.error("error adding " + e.toString());
            throw new RuntimeException(e);
        }

    }

    public void updateUser(AppUser user){
        repo.save(user);
    }

    public void deleteUser(Long id){
        repo.deleteById(id);
        log.info("deleted user with id: " + id);
    }
}
