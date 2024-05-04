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
        private final UserRepository userRepository;

//        public SchoolDto createSchool(String name, String emailDomain, String userEmail) {
//
//            AppUser user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//            List<AppUser> users = new ArrayList<>();
//            users.add(user);
//            // Create a new school entity
//            School school = new School();
//            school.setName(name);
//            school.setEmailDomain(emailDomain);
//           // school.setUsers(users);
//
//            school = schoolRepository.save(school);
//
//            // Map the school entity to a school DTO
//            return new SchoolDto(school.getName(), school.getEmailDomain(),user.getId());
//        }
        public School createSchool(String name, String emailDomain) {
            School school = new School();
            school.setName(name);
            school.setEmailDomain(emailDomain);
            return schoolRepository.save(school);
        }


//        public List<School> getAllSchools() {
//            //List<SchoolDto> dtoList = new LinkedList<>();
//
//            List<School> schoolList = schoolRepository.findAll();
//           // schoolList.forEach(school -> {
//             //   dtoList.add(new SchoolDto( school.getName(),school.getEmailDomain()));
//
//          //  });
//            return schoolList;
//
//        }

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


//        public List<School> getAllSchools() {
//            return schoolRepository.findAllSchools();
//        }
        public Optional<School> findSchoolsByEmail(String emailDomain) {
            return schoolRepository.findByEmailDomain(emailDomain);
        }

    }

