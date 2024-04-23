package com.accesskeymanager.AccessKeyManager.model;

import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@Table(name = "access_keys")
public class AccessKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private KeyStatus status;

    @Column(name = "date_of_procurement")
    private LocalDate dateOfProcurement;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    // constructors
    public AccessKey() {
    }
}
