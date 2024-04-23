package com.accesskeymanager.AccessKeyManager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class AppUser {
    // getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Enter a valid email")
    private String email;

    @NotNull(message = "Password cannot be empty")
    private String password;

    @ManyToOne()
    @JoinColumn(name = "school_id")
    private School school;


    @Enumerated(EnumType.STRING)
    private Role role;
}
