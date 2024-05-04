package com.accesskeymanager.AccessKeyManager.service;

import com.accesskeymanager.AccessKeyManager.model.School;
import com.accesskeymanager.AccessKeyManager.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

    @Service
    public class SchoolService {
        @Autowired
        private SchoolRepository schoolRepository;

        public School createSchool(String name, String emailDomain) {
            School school = new School();
            school.setName(name);
            school.setEmailDomain(emailDomain);
            return schoolRepository.save(school);
        }

        public List<School> getAllSchools() {
            return schoolRepository.findAll();
        }

        public Optional<School> getSchoolById(Long id) {

            return schoolRepository.findById(id);
        }

        public Optional<School> findByEmailDomain(String emailDomain) {
            return schoolRepository.findByEmailDomain(emailDomain);
        }
        public Optional<School> findById(Long id) {
            return schoolRepository.findById(id);
        }
    }

