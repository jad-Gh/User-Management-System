package com.example.UserManagementSystem.AppUser;

import com.example.UserManagementSystem.Roles.AppRole;
import com.example.UserManagementSystem.Roles.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service @RequiredArgsConstructor @Slf4j
public class AppUserService {

    private final AppUserRepo repo;
    private final RoleRepository roleRepo;

    public List<AppUser> getUsers(){
        try{
            log.info("retrieving users...");
            return repo.findAll();
        }catch(Exception e){
            log.error("error retrieving users "+ e.toString());
            throw new RuntimeException(e);
        }
    }

    public List<AppRole> getRoles(){
        try{
            return roleRepo.findAll();
        }catch(Exception e) {
            log.error("error retrieving roles: "+ e.toString());
            throw new RuntimeException(e);
        }
    }

    public void addUser(AppUser user){
        try{
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setRole(roleRepo.getById(user.getRole().getId()));
            repo.save(user);
            log.info("user saved");
        }catch(Exception e){
            log.error("error adding new user: " + e.toString());
            throw new RuntimeException(e);
        }
    }

    public void updateUser(AppUser user){
        try {
            if (repo.findById(user.getId()).isEmpty()){
                log.info("user with id:{} not found",user.getId());
                throw new RuntimeException("User not Found");
            }else {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                user.setRole(roleRepo.getById(user.getRole().getId()));
                log.info("user with id:{} updated",user.getId());
                repo.save(user);
            }
        }catch (Exception e){
            log.error("error updating user with id: "+user.getId());
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(Long id){
        try{
            repo.deleteById(id);
            log.info("deleted user with id: " + id);
        }catch(Exception e){
            log.error("error deleting user with id: " + id);
            throw new RuntimeException(e);
        }
    }
}
