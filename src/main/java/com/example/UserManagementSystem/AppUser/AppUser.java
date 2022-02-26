package com.example.UserManagementSystem.AppUser;

import com.example.UserManagementSystem.Roles.AppRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private String profileImage;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean active;
    @Column(nullable = false)
    private boolean locked;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime lastLogin;
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    private AppRole role;
}
