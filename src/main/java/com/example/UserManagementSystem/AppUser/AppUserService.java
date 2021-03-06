package com.example.UserManagementSystem.AppUser;

import com.example.UserManagementSystem.Roles.AppRole;
import com.example.UserManagementSystem.Roles.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.*;

@Service @RequiredArgsConstructor @Slf4j
public class AppUserService {

    private final AppUserRepo repo;
    private final RoleRepository roleRepo;

    @Cacheable(value="Users")
    public Map<String,Object> getUsers(int page,int size,String email){
        try{
            log.info("retrieving users...");
//            return repo.findAll();
            Pageable paging = PageRequest.of(page,size, Sort.by("email"));
            Page<AppUser> pageUsers =repo.findByEmailContaining(email,paging);
            Map<String,Object> response= new HashMap<>();
            response.put("Total Elements",pageUsers.getTotalElements());
            response.put("Total Pages",pageUsers.getTotalPages());
            response.put("Current Page",pageUsers.getNumber());
            response.put("Users",pageUsers.getContent());
            return response;

        }catch(Exception e){
            log.error("error retrieving users "+ e.toString());
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = "Roles")
    public List<AppRole> getRoles(){
        try{
            return roleRepo.findAll();
        }catch(Exception e) {
            log.error("error retrieving roles: "+ e.toString());
            throw new RuntimeException(e);
        }
    }


    @CacheEvict(value = "Users",allEntries = true)
    public void addUser(AppUser user){
        try{
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setRole(roleRepo.getById(user.getRole().getId()));
            user.setLocked(true);
            repo.save(user);
            log.info("user saved");
        }catch(Exception e){
            log.error("error adding new user: " + e.toString());
            throw new RuntimeException(e);
        }
    }

    @CacheEvict(value = "Users",allEntries = true)
    public void updateUser(AppUser user){
        try {
            if (repo.findById(user.getId()).isEmpty()){
                log.info("user with id:{} not found",user.getId());
                throw new RuntimeException("User not Found");
            }else {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                user.setRole(roleRepo.getById(user.getRole().getId()));
                user.setProfileImage(repo.getById(user.getId()).getProfileImage());
                log.info("user with id:{} updated",user.getId());
                repo.save(user);
            }
        }catch (Exception e){
            log.error("error updating user with id: "+user.getId());
            throw new RuntimeException(e);
        }
    }

    @CacheEvict(value = "Users",allEntries = true)
    public void uploadPhoto(Long id,MultipartFile photo) throws IOException {
            if (repo.findById(id).isEmpty()){
                log.info("user with id:{} not found",id);
                throw new RuntimeException("User associated with photo not found");
            }
            AppUser user = repo.getById(id);
            String folder = "/AppUserProjectPhotos/";
            byte [] bytes = photo.getBytes();
            String photoName = System.currentTimeMillis()+ photo.getOriginalFilename();
            Path path = Paths.get(folder + photoName);
            Files.write(path,bytes);
            user.setProfileImage(photoName);
            repo.save(user);
    }

    @CacheEvict(value = "Users",allEntries = true)
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
