package com.example.UserManagementSystem.AppUser;



import com.example.UserManagementSystem.Excel.UserExcelExporter;
import com.example.UserManagementSystem.RegistrationService.RegistrationService;
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


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService service;
    private final RegistrationService registrationService;

    @GetMapping
    public ResponseEntity<Response> getAllUsers (@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "") String email){
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Success")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("Content",service.getUsers(page,size,email))).build()
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

    @GetMapping("/report")
    public void exportToExcel(HttpServletResponse response)throws IOException{
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<AppUser> listUsers = List.of((AppUser) service.getUsers(0,10,"").get("Users"));

        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

        excelExporter.export(response);

    }

    @PostMapping("/add")
    public ResponseEntity<Response> addUser (@Valid @RequestBody AppUser user){
//        service.addUser(user);
        registrationService.registerUser(user);
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

    @GetMapping("/reset-password/{email}")
    public ResponseEntity<Response> resetPassword(@PathVariable("email") String email){
        registrationService.sendResetPasswordEmail(email);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Email Sent successfully")
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
