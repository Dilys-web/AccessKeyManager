package com.accesskeymanager.AccessKeyManager.controller;

import com.accesskeymanager.AccessKeyManager.DTO.request.AccessKeyDto;
import com.accesskeymanager.AccessKeyManager.DTO.request.AccessKeyStatusDto;
import com.accesskeymanager.AccessKeyManager.model.AccessKey;
import com.accesskeymanager.AccessKeyManager.service.AccessKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth/access-keys")
public class AccessKeyController {

    @Autowired
    private AccessKeyService accessKeyService;

//    @GetMapping
//    public ResponseEntity<List<AccessKey>> getAllAccessKeys() {
//        List<AccessKey> accessKeys = accessKeyService.getAllAccessKeys();
//        return new ResponseEntity<>(accessKeys, HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessKey> getAccessKeyById(@PathVariable Long id) {
        return accessKeyService.getAccessKeyById(id)
                .map(accessKey -> new ResponseEntity<>(accessKey, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AccessKeyDto> createAccessKey(@RequestBody AccessKeyDto accessKeyDto) {
        AccessKeyDto createdAccessKey = accessKeyService.generateAccessKey(accessKeyDto);
        return new ResponseEntity<>(createdAccessKey, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessKey> updateAccessKey(@PathVariable Long id, @RequestBody AccessKey accessKey) {
        if (!accessKeyService.getAccessKeyById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        accessKey.setId(id);
        AccessKey updatedAccessKey = accessKeyService.updateAccessKey(accessKey);
        return new ResponseEntity<>(updatedAccessKey, HttpStatus.OK);
    }

    @DeleteMapping("access-keys/{id}")
    public ResponseEntity<Void> revokeAccessKey(@PathVariable Long id) {
        Optional<AccessKey> accessKeyOptional = accessKeyService.getAccessKeyById(id);
        if (accessKeyOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AccessKey accessKey = accessKeyOptional.get(); // Retrieve the AccessKey object from Optional
        accessKeyService.revokeAccessKey(accessKey);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    public ResponseEntity<AccessKeyStatusDto>checkAccessKeyStatus(@PathVariable String accessKey) {
        AccessKeyStatusDto statusDto = accessKeyService.checkAccessKeyStatus(accessKey);
        if (statusDto != null) {
            return new ResponseEntity<>(statusDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


