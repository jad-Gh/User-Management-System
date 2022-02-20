package com.example.UserManagementSystem.AppUser;



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

    @PostMapping("/add")
    public ResponseEntity<String> addUser (@RequestBody AppUser user){
        service.addUser(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}/")
    public ResponseEntity<String> deleteUser (@PathVariable long id){
        service.deleteUser(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }



}
