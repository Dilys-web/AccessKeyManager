package com.accesskeymanager.AccessKeyManager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        @Setter
@Getter
@Entity
public class School {
    // getters and setters
    @Id
    @Column(name = "school_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "School name cannot be null")
    private String name;

    @Column(name = "email_domain", unique = true)
    @NotNull(message = "Email cannot be null")
    private String emailDomain;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    private List<AppUser> users = new ArrayList<>();
}

