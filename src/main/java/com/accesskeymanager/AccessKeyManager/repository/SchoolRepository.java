package com.accesskeymanager.AccessKeyManager.repository;


import com.accesskeymanager.AccessKeyManager.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    // You can add custom query methods here if needed
    Optional<School> findByEmailDomain(String emailDomain);

    boolean existsByEmailDomain(String email);

 Optional<School>findAllByEmailDomain(String emailDomain);

}
