package com.example.UserManagementSystem.AppUser;



import com.example.UserManagementSystem.Roles.AppRole;
import com.example.UserManagementSystem.Roles.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService service;

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers (){
        return ResponseEntity.ok().body(service.getUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<AppRole>> getRoles (){
        return ResponseEntity.ok().body(service.getRoles());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser (@RequestBody AppUser user){
        try{
            service.addUser(user);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUser (@RequestBody AppUser user){
        service.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}/")
    public ResponseEntity<String> deleteUser (@PathVariable long id){
        service.deleteUser(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }
}
