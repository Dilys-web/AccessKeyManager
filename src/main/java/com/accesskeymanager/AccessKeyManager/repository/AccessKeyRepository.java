package com.accesskeymanager.AccessKeyManager.repository;

import com.accesskeymanager.AccessKeyManager.model.AccessKey;
import com.accesskeymanager.AccessKeyManager.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AccessKeyRepository extends JpaRepository<AccessKey, Long> {
    List<AccessKey> findByUser(AppUser user);
    // You can add custom query methods here if needed
}