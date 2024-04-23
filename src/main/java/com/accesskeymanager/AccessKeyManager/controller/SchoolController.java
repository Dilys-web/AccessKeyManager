package com.accesskeymanager.AccessKeyManager.controller;

import com.accesskeymanager.AccessKeyManager.DTO.CreateSchoolRequest;
import com.accesskeymanager.AccessKeyManager.model.School;
import com.accesskeymanager.AccessKeyManager.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
// @RequestMapping("/api")
public class SchoolController {

    // @Autowired
    private final SchoolService schoolService;
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get-schools")
    // public School getSchoolById(@PathVariable Long id) {
        public School getSchoolById() {
        System.out.println("School");
         Optional<School> schoolOptional = schoolService.findById(id);

         if (schoolOptional.isPresent()) {
             //return ResponseEntity.ok(schoolOptional.get());
             return schoolOptional.orElseThrow();
         } else {
             //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("School not found.");
             return null;
         }
        return null;
    }

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
}