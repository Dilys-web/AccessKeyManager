package com.accesskeymanager.AccessKeyManager.service;

import com.accesskeymanager.AccessKeyManager.DTO.request.SchoolDto;
import com.accesskeymanager.AccessKeyManager.model.AppUser;
import com.accesskeymanager.AccessKeyManager.model.School;
import com.accesskeymanager.AccessKeyManager.repository.SchoolRepository;
import com.accesskeymanager.AccessKeyManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

    @Service
    @RequiredArgsConstructor
    public class SchoolService {

        private final SchoolRepository schoolRepository;


        public School createSchool(String name, String emailDomain) {

            School school = new School();
            school.setName(name);
            school.setEmailDomain(emailDomain);
            return schoolRepository.save(school);
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

//        public SchoolDto  update(SchoolDto dto){
//
//            if (dto.id()!=null) {
//                Optional<School> school = findById(dto.id());
//                if (school.isPresent()){
//                    School s = school.get();
//                    s.setName(dto.name());
//                    s.setEmailDomain(dto.emailDomain());
//                    s.setId(dto.id());
//
//                    s = schoolRepository.save(s);
//                    if(s!= null){
//                        dto = new SchoolDto(s.getId(), s.getName(), s.getEmailDomain());
//                    }
//                    return dto;
//
//                }
//
//            }
//            return null;
//        }

        public void revokeSchool(School school) {
            schoolRepository.delete(school);
        }


        public Optional<School> findSchoolsByEmail(String emailDomain) {
            return schoolRepository.findByEmailDomain(emailDomain);
        }

    }

