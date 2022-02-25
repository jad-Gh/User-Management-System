package com.example.UserManagementSystem.Roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String RoleName;
    private boolean read;
    private boolean create_auth;
    private boolean delete;
    private boolean update;

    public AppRole(String RoleName,boolean read,boolean create,boolean delete,boolean update){
        this.RoleName=RoleName;
        this.read=read;
        this.create_auth=create;
        this.delete=delete;
        this.update=update;
    }

}
