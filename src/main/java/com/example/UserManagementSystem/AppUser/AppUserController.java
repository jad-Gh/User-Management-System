package com.example.UserManagementSystem.AppUser;



import com.example.UserManagementSystem.Response.Response;
import com.example.UserManagementSystem.Roles.AppRole;
import com.example.UserManagementSystem.Roles.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService service;

    @GetMapping
    public ResponseEntity<Response> getAllUsers (){
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Success")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("Users",service.getUsers())).build()
        );
    }

    @GetMapping("/roles")
    public ResponseEntity<Response> getRoles (){
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Success")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("Roles",service.getRoles())).build()
        );
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addUser (@Valid @RequestBody AppUser user){
        service.addUser(user);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Successfully added user")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PostMapping("/update")
    public ResponseEntity<Response> updateUser (@Valid @RequestBody AppUser user){
        service.updateUser(user);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message("User Updated successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .build()
        );
    }
    @PostMapping("/upload-photo")
    public ResponseEntity<Response> uploadPhoto (@RequestParam Long id,@RequestPart("photo") MultipartFile photo) throws IOException {
        service.uploadPhoto(id,photo);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Profile picture uploaded successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/get-photo/{filename}/")
    public ResponseEntity<Resource> getPhoto (@PathVariable("filename") String filename) throws IOException{
        Path path = Paths.get("/AppUserProjectPhotos/" + filename);
        if(!Files.exists(path)) {
            throw new FileNotFoundException(filename + " was not found on the server");
        }
        Resource resource = new UrlResource(path.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(path))).headers(httpHeaders).body(resource);
    }

    @DeleteMapping("/delete/{id}/")
    public ResponseEntity<Response> deleteUser (@PathVariable long id){
        service.deleteUser(id);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message("User Successfully deleted")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
