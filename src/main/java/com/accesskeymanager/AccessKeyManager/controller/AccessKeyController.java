package com.accesskeymanager.AccessKeyManager.controller;

import com.accesskeymanager.AccessKeyManager.DTO.request.AccessKeyDto;
import com.accesskeymanager.AccessKeyManager.DTO.request.AccessKeySchoolDto;
import com.accesskeymanager.AccessKeyManager.DTO.response.AccessKeyResponseDto;
import com.accesskeymanager.AccessKeyManager.Exception.OperationFailedException;
import com.accesskeymanager.AccessKeyManager.service.AccessKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accesskeys/")
@AllArgsConstructor
public class AccessKeyController {

    private final AccessKeyService accessKeyService;

    @GetMapping("all")
    @Operation(summary = "For admin getting all access keys generated on the platform")
    public ResponseEntity<List<AccessKeySchoolDto>> getAllAccessKeys() {
        List<AccessKeySchoolDto> accessKeys = accessKeyService.getAllAccessKeys();
        return new ResponseEntity<>(accessKeys, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
            return ResponseEntity.ok("Test");
        }

    @GetMapping("request/{schoolId}")
    @Operation(summary = "For generating access keys")
    public ResponseEntity<AccessKeyResponseDto> generateAccessKey(@PathVariable("schoolId") Long schoolId) {
        System.out.println("hiii");
        // Retrieve authenticated user details
        UserDetails authentication = getCurrentUser();
       String userEmail = authentication.getUsername();
        System.out.println(userEmail);
        System.out.println("aaaaaaaa");

        try {
            // Generate the access key for the authenticated user
            AccessKeyResponseDto createdAccessKey = accessKeyService.generateAccessKey(userEmail,schoolId);
            return new ResponseEntity<>(createdAccessKey, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that occur during access key generation
            throw new OperationFailedException(e.getMessage());
        }
    }

    @GetMapping("all/{userId}")
    @Operation(summary = "Finding the accesskeys for a user")
    public ResponseEntity<List<AccessKeyDto>> getAllAccessKeysForUser(@PathVariable Long userId){
        List<AccessKeyDto> accessKeys = accessKeyService.getAllAccessKeysForUser(userId);
        return new ResponseEntity<>(accessKeys, HttpStatus.OK);

    }

    @GetMapping("active/{schoolEmail}")// when sch email was entered it says sch not found
    public ResponseEntity<AccessKeyResponseDto> getAccessKeyForSchool(@PathVariable String schoolEmail){
        return  ResponseEntity.ok(accessKeyService.getAccessKeyForSchool(schoolEmail));
    }
    public UserDetails getCurrentUser() {
        // Get the authentication object from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is not null and contains UserDetails
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Return the username of the authenticated user
            return userDetails;
        } else {
            // If no user is authenticated, return a default message
            throw new BadCredentialsException("User not found");
        }
    }

    @GetMapping("{keyId}")//: "No static resource api/v1/accesskeys/5."
    @Operation(summary = "For getting the details of an access key")
    public ResponseEntity<AccessKeyResponseDto> getAccessKey(@PathVariable Long keyId){
        return ResponseEntity.ok(accessKeyService.getAccessKey(keyId));
    }
//    @DeleteMapping("revoke/{keyid}")
//    @Operation(summary = "For deleting an access key")// no parameters
//    public ResponseEntity<Void> revokeAccessKey(@Parameter(description = "key ID", in = ParameterIn.PATH) @PathVariable Long id) {
//
//        accessKeyService.revokeAccessKey(id);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

//    @GetMapping(value = "revoke/{keyid}")
//    @Operation(summary = "For deleting an access key")// no parameters
//    public ResponseEntity<Void> revokeAccessKey(@Parameter( required = true, description = "key ID") @PathVariable Long id) {
//
//        accessKeyService.revokeAccessKey(id);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @DeleteMapping("revoke/{keyId}")//: "No static resource api/v1/accesskeys/5."
    @Operation(summary = "For deleting the details of an access key")
    public ResponseEntity<Void> revokeAccessKey(@PathVariable Long keyId){
        accessKeyService.revokeAccessKey(keyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


