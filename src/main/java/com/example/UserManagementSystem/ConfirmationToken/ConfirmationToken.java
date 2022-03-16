package com.example.UserManagementSystem.ConfirmationToken;

import com.example.UserManagementSystem.AppUser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne()
    @JoinColumn(nullable = false,name="app_user_id")
    private AppUser user;

    public ConfirmationToken(String token,LocalDateTime exp,AppUser user){
        this.token = token;
        this.expirationDate = exp;
        this.user = user;
    }
}
