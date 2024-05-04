package com.accesskeymanager.AccessKeyManager.model;

import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "access_key")
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

    @Column(name = "key_status")
    @Enumerated(EnumType.STRING)
    private KeyStatus status;

    @Column(name = "date_of_procurement")
    @CreationTimestamp
    private LocalDate dateOfProcurement;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "access_key")
    private String accessKey;


}
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public AppUser getUser() {
//        return user;
//    }
//
//    public void setUser(AppUser user) {
//        this.user = user;
//    }
//
//    public School getSchool() {
//        return school;
//    }
//
//    public void setSchool(School school) {
//        this.school = school;
//    }
//
//    public KeyStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(KeyStatus status) {
//        this.status = status;
//    }
//
//    public LocalDate getDateOfProcurement() {
//        return dateOfProcurement;
//    }
//
//    public void setDateOfProcurement(LocalDate dateOfProcurement) {
//        this.dateOfProcurement = dateOfProcurement;
//    }
//
//    public LocalDate getExpiryDate() {
//        return expiryDate;
//    }
//
//    public void setExpiryDate(LocalDate expiryDate) {
//        this.expiryDate = expiryDate;
//    }
//
//    public String getAccessKey() {
//        return accessKey;
//    }
//
//    public void setAccessKey(String accessKey) {
//        this.accessKey = accessKey;
//    }
//}
