// package com.accesskeymanager.AccessKeyManager.service;

// import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;
// import com.accesskeymanager.AccessKeyManager.model.AccessKey;
// import com.accesskeymanager.AccessKeyManager.model.AppUser;
// import com.accesskeymanager.AccessKeyManager.model.School;
// import com.accesskeymanager.AccessKeyManager.repository.AccessKeyRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.Optional;

// @Service
// public class AccessKeyService {
//     @Autowired
//     private AccessKeyRepository accessKeyRepository;

//     public AccessKey generateAccessKey(AppUser user, School school, KeyStatus status, LocalDate dateOfProcurement, LocalDate expiryDate) {
//         AccessKey accessKey = new AccessKey();
//         accessKey.setUser(user);
//         accessKey.setSchool(school);
//         accessKey.setStatus(status);
//         accessKey.setDateOfProcurement(dateOfProcurement);
//         accessKey.setExpiryDate(expiryDate);
//         return accessKeyRepository.save(accessKey);
//     }

//     public void revokeAccessKey(AccessKey accessKey) {
//         accessKeyRepository.delete(accessKey);
//     }

//     public List<AccessKey> getAllAccessKeys() {

//         return accessKeyRepository.findAll();
//     }

//     public List<AccessKey> getAccessKeysByUser(AppUser user) {

//         return accessKeyRepository.findByUser(user);
//     }

//     public Optional<AccessKey> getAccessKeyById(Long id) {

//         return accessKeyRepository.findById(id);
//     }

//     public AccessKey createAccessKey(AccessKey accessKey) {
//         return accessKeyRepository.save(accessKey);
//     }
//     public boolean hasActiveAccessKey(AppUser user) {
//         List<AccessKey> accessKeys = accessKeyRepository.findByUser(user);
//         for (AccessKey accessKey : accessKeys) {
//             if (accessKey.getStatus() == KeyStatus.ACTIVE && accessKey.getExpiryDate().isAfter(LocalDate.now())) {
//                 return true;
//             }
//         }
//         return false;
//     }

// public AccessKey updateAccessKey(AccessKey updatedAccessKey) {
//     Optional<AccessKey> existingAccessKeyOptional = accessKeyRepository.findById(updatedAccessKey.getId());

//     if (existingAccessKeyOptional.isPresent()) {
//         AccessKey existingAccessKey = existingAccessKeyOptional.get();

//         existingAccessKey.setUser(updatedAccessKey.getUser());
//         existingAccessKey.setSchool(updatedAccessKey.getSchool());
//         existingAccessKey.setStatus(updatedAccessKey.getStatus());
//         existingAccessKey.setDateOfProcurement(updatedAccessKey.getDateOfProcurement());
//         existingAccessKey.setExpiryDate(updatedAccessKey.getExpiryDate());

//         return accessKeyRepository.save(existingAccessKey);
//     } else {
//         // Handle the case where the access key with the provided ID is not found
//         return null;
//     }
// }
// }

