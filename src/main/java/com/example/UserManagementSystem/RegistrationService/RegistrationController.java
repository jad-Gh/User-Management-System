package com.example.UserManagementSystem.RegistrationService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @GetMapping("/confirm")
    public String confirmToken(@RequestParam("token") String token){
        return registrationService.enableUser(token);
    }

    @GetMapping("/reset")
    public String resetPasswordToken(@RequestParam("token") String token){
        return registrationService.resetUserPassword(token);
    }

}
