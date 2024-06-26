package com.accesskeymanager.AccessKeyManager.repository;

import com.accesskeymanager.AccessKeyManager.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    // You can add custom query methods here if needed

}