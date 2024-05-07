package com.accesskeymanager.AccessKeyManager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appUser")
public class AppUser {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Enter a valid email")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Password cannot be empty")
    private String password;

    @ManyToOne()
    @JoinColumn(name = "school_id", referencedColumnName = "school_id")
    private School school;

    @Enumerated(EnumType.STRING)
    private Role role;

//    @Column(name = "is_verified")
    private boolean isVerified;

//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private Date createdAt;
//
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private Date updatedAt;
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


//  "message": "could not execute statement [ERROR: column \"created_at\" of relation \"app_user\" does not exist\n  Position: 23\n  Location: File: parse_target.c, Routine: checkInsertTargets, Line: 1057\n  Server SQLState: 42703] [insert into app_user (created_at,email,is_verified,password,role,school_id) values (?,?,?,?,?,?)]; SQL [insert into app_user (created_at,email,is_verified,password,role,school_id) values (?,?,?,?,?,?)]",
}
