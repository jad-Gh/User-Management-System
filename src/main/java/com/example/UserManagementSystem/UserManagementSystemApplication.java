package com.example.UserManagementSystem;

import com.example.UserManagementSystem.Roles.AppRole;
import com.example.UserManagementSystem.Roles.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class UserManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(RoleRepository repository) {
		return (args) -> {
			repository.saveAll(
					List.of(
							new AppRole("ROLE_USER",true,false,false,false),
							new AppRole("ROLE_MODERATOR",true,false,false,true),
							new AppRole("ROLE_MANAGER",true,true,false,true),
							new AppRole("ROLE_ADMIN",true,true,true,true)
					)

			);
		};
	}
}
