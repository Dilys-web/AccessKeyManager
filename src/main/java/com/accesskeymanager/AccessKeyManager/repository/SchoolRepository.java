package com.accesskeymanager.AccessKeyManager.repository;


import com.accesskeymanager.AccessKeyManager.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    // You can add custom query methods here if needed
    School findByEmailDomain(String emailDomain);
}
