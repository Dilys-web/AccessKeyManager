package com.accesskeymanager.AccessKeyManager.repository;

import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;
import com.accesskeymanager.AccessKeyManager.model.AccessKey;
import com.accesskeymanager.AccessKeyManager.model.AppUser;
import com.accesskeymanager.AccessKeyManager.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface AccessKeyRepository extends JpaRepository<AccessKey, Long> {
    List<AccessKey> findByUser(AppUser user);
    // You can add custom query methods here if needed
    Optional<AccessKey> findBySchoolAndStatus(School school, KeyStatus status);
    boolean existsByUserAndStatus(AppUser user, KeyStatus status);
}