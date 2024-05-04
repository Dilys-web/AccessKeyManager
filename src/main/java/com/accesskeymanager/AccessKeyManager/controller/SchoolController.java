package com.accesskeymanager.AccessKeyManager.controller;

import com.accesskeymanager.AccessKeyManager.DTO.request.CreateSchoolRequest;
import com.accesskeymanager.AccessKeyManager.DTO.request.SchoolDto;
import com.accesskeymanager.AccessKeyManager.model.School;
import com.accesskeymanager.AccessKeyManager.service.SchoolService;
import com.accesskeymanager.AccessKeyManager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/")
public class SchoolController {

    private final SchoolService schoolService;
    private final UserService userService;

    @GetMapping("/get-schools/{id}")
     public ResponseEntity<SchoolDto>  getSchoolById(@PathVariable Long id) {
        System.out.println("School");
         Optional<School> schoolOptional = schoolService.findById(id);

         if (schoolOptional.isPresent()) {
             School school = schoolOptional.get();
             return ResponseEntity.ok(new SchoolDto(school.getName(), school.getEmailDomain(), school.getId()));
         }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

//    @PostMapping("/schools")
//    public ResponseEntity<?> createSchool(@RequestBody SchoolDto schoolDto, Principal principal) {
//        // Validate request
//
//        if (schoolDto.name() == null || schoolDto.emailDomain() == null) {
//            return ResponseEntity.badRequest().body("Name and email domain are required.");
//        }
//
//        // Check if school already exists
//        if (schoolService.findByEmailDomain(schoolDto.emailDomain()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("School with this email domain already exists.");
//        }
//
//        if(principal != null){
//            // Create new school
//
//            SchoolDto newSchool = schoolService.createSchool(schoolDto.name(), schoolDto.emailDomain(), principal.getName());
//            return ResponseEntity.ok(newSchool);
//
//        }
//        return null;
//    }
    @PostMapping("/schools")
    public ResponseEntity<?> createSchool(@RequestBody CreateSchoolRequest createSchoolRequest) {
        // Validate request
        if (createSchoolRequest.getName() == null || createSchoolRequest.getEmailDomain() == null) {
            return ResponseEntity.badRequest().body("Name and email domain are required.");
        }

        // Check if school already exists
        Optional<School> existingSchool = schoolService.findByEmailDomain(createSchoolRequest.getEmailDomain());
        if (existingSchool.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("School with this email domain already exists.");
        }

        // Create new school
        School newSchool = schoolService.createSchool(createSchoolRequest.getName(), createSchoolRequest.getEmailDomain());
        return ResponseEntity.ok(newSchool);
    }



    @PutMapping("/schools")
//    public ResponseEntity<SchoolDto> updateSchool(@RequestBody SchoolDto schoolDto) {
//
//        // Create new school
//        SchoolDto newSchool = schoolService.update(schoolDto);
//        return ResponseEntity.ok(newSchool);
//    }

    @DeleteMapping("schools/{id}")
    public ResponseEntity<Void> revokeSchool(@PathVariable Long id) {
        Optional<School> schoolOptional = schoolService.getSchoolById(id);
        if (schoolOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        School school = schoolOptional.get(); // Retrieve the AccessKey object from Optional
        schoolService.revokeSchool(school);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
//    @GetMapping("schools/email={email}")
//    public Optional<School> getAllSchools(@PathVariable String email) {
//
//        return schoolService.findSchoolsByEmail(email);
//
//    }

//    @GetMapping("schools")
//    public List<School> getAllSchools() {
//
//        return schoolService.getAllSchools();
//
//    }
}

