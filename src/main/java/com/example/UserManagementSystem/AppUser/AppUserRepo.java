package com.example.UserManagementSystem.AppUser;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findByEmail(String email);
    Page<AppUser> findByEmailContaining(String email, Pageable pageable);
}
