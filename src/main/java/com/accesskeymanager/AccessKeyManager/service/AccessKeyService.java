 package com.accesskeymanager.AccessKeyManager.service;

 import com.accesskeymanager.AccessKeyManager.DTO.request.AccessKeyDto;
 import com.accesskeymanager.AccessKeyManager.DTO.response.AccessKeyResponseDto;
 import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;
 import com.accesskeymanager.AccessKeyManager.Exception.OperationFailedException;
 import com.accesskeymanager.AccessKeyManager.model.AccessKey;
 import com.accesskeymanager.AccessKeyManager.model.AppUser;
 import com.accesskeymanager.AccessKeyManager.model.School;
 import com.accesskeymanager.AccessKeyManager.repository.AccessKeyRepository;
 import com.accesskeymanager.AccessKeyManager.repository.SchoolRepository;
 import com.accesskeymanager.AccessKeyManager.repository.UserRepository;
 import jakarta.persistence.EntityExistsException;
 import jakarta.persistence.EntityNotFoundException;
 import lombok.RequiredArgsConstructor;
 import org.springframework.stereotype.Service;

 import java.time.LocalDate;
 import java.time.LocalDateTime;
 import java.util.List;
 import java.util.UUID;
 import java.util.stream.Collectors;


 @Service
 @RequiredArgsConstructor
 public class AccessKeyService {

     private final AccessKeyRepository accessKeyRepository;
     private final UserRepository userRepository;
     private final SchoolRepository schoolRepository;
//     private static final Logger logger = Logger.getLogger(AccessKeyService.class.getName());


     public AccessKeyResponseDto generateAccessKey(String userEmail, Long schoolId) {
         System.out.println(userEmail);
         AppUser user = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("User does not exist"));

         try {
             // Check if the user already has an active key
             boolean isAccessKeyExists = accessKeyRepository.existsByUserAndStatus(user, KeyStatus.ACTIVE);
             if (isAccessKeyExists) {
                 throw new EntityExistsException("User already has an active access key");
             }

             // Generate a new access key
             AccessKey accessKey = new AccessKey();
             accessKey.setAccessKey(generateKey());
             accessKey.setStatus(KeyStatus.ACTIVE);
             accessKey.setDateOfProcurement(LocalDate.from(LocalDateTime.now()));
             accessKey.setExpiryDate(LocalDate.from(LocalDateTime.now().plusYears(1))); // Assuming one year expiry
             accessKey.setSchool(user.getSchool());
             accessKey.setUser(user);

             AccessKey savedKey = accessKeyRepository.save(accessKey);

             // Map the saved access key to AccessKeyDto and return
             return mapToDto(savedKey);
         } catch (Exception e) {
             // Handle exceptions
             throw new OperationFailedException("Failed to generate access key: " + e.getMessage());
         }
     }

     public void markAsExpired(Long accessKeyId) {
         try {
             AccessKey accessKey = accessKeyRepository.findById(accessKeyId)
                     .orElseThrow(() -> new IllegalArgumentException("Access key not found"));
             accessKey.setStatus(KeyStatus.EXPIRED);
             accessKeyRepository.save(accessKey);
         } catch (Exception e) {
             // Handle exceptions
             throw new OperationFailedException("Failed to mark access key as expired: " + e.getMessage());
         }
     }

     private String generateKey() {
         return UUID.randomUUID().toString();
     }

     private AccessKeyResponseDto mapToDto(AccessKey accessKey) {
         return new AccessKeyResponseDto(
                 accessKey.getAccessKey(),
                 accessKey.getId(),
                 accessKey.getStatus(),
                 accessKey.getExpiryDate(),
                 accessKey.getDateOfProcurement()


         );
     }


     public void revokeAccessKey(Long keyId) {
         AccessKey accessKey = accessKeyRepository.findById(keyId).orElseThrow(() -> new EntityNotFoundException("Access key not found"));

         if (KeyStatus.ACTIVE.equals(accessKey.getStatus())) {
             accessKey.setStatus(KeyStatus.REVOKED);
             accessKeyRepository.save(accessKey);

         } else {
             throw new IllegalStateException("Access key is not active");
         }
     }

     public AccessKeyResponseDto getAccessKey(Long keyId){
         AccessKey accessKey = accessKeyRepository.findById(keyId).orElseThrow(() -> new EntityNotFoundException("Access key not found"));
         return new AccessKeyResponseDto(
                 accessKey.getAccessKey(),
                 accessKey.getId(),
                 accessKey.getStatus(),
                 accessKey.getExpiryDate(),
                 accessKey.getDateOfProcurement()
         );
     }

     public List<AccessKeyDto> getAllAccessKeys() {
         List<AccessKey> accessKeys = accessKeyRepository.findAll();
         return accessKeys.stream()
                 .map(this::generateDtoFromAccessKey)
                 .collect(Collectors.toList());
     }
     private AccessKeyDto generateDtoFromAccessKey(AccessKey accessKey) {
         return new AccessKeyDto(
                 accessKey.getId(),
                 accessKey.getAccessKey(),
                 accessKey.getStatus(),
                 accessKey.getDateOfProcurement(),
                 accessKey.getExpiryDate()

         );
     }
     public List<AccessKeyDto> getAllAccessKeysForUser(Long userId){
         AppUser user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
         List<AccessKey>accessKeys =accessKeyRepository.findByUser(user);
         return accessKeys.stream().map(accessKey -> {
             return new AccessKeyDto(
                     accessKey.getId(),
                     accessKey.getAccessKey(),
                     accessKey.getStatus(),
                     accessKey.getDateOfProcurement(),
                     accessKey.getExpiryDate()
             );
                 }).toList();
     }

     public AccessKeyResponseDto getAccessKeyForSchool(String schoolEmail){
         School school = schoolRepository.findByEmailDomain(schoolEmail).orElseThrow(() -> new EntityNotFoundException("School not found"));
         AccessKey accessKey = accessKeyRepository.findBySchoolAndStatus(school, KeyStatus.ACTIVE).orElseThrow(() -> new EntityNotFoundException("Active Access key not found"));
         return new AccessKeyResponseDto(
                 accessKey.getAccessKey(),
                 accessKey.getId(),
                 accessKey.getStatus(),
                 accessKey.getExpiryDate(),
                 accessKey.getDateOfProcurement()
         );
     }
 }


//     public AccessKeyDto generateAccessKey(String userEmail, Long schoolId ) {
//
//         Optional<AppUser> user = userRepository.findByEmail(userEmail);
//         Optional<School> school = schoolRepository.findById(schoolId);
//         AccessKeyDto dto = null;
//
//         AccessKey accessKey = new AccessKey();
//         if(user.isEmpty()){
//             throw new RuntimeException("User is not found.");
//         }
//         if(school.isEmpty()){
//             throw new RuntimeException("User is not found.");
//         }
//         accessKey.setUser(user.get());
//         accessKey.setSchool(school.get());
//         accessKey.setAccessKey(genId().toUpperCase());
//         AccessKey access = accessKeyRepository.save(accessKey);
//
//        if(access != null){
//         dto = new AccessKeyDto(access.getId(),access.getUser().getEmail(),access.getUser().getId(), access.getSchool().getName(), access.getSchool().getId(),
//                 access.getStatus(), access.getDateOfProcurement(), access.getExpiryDate(), access.getAccessKey(), checkExpiry(access.getExpiryDate()));
//        }
//         return dto;
//     }
//     public String genId() {
//         try {
//             String id = UUID.randomUUID().toString().replaceAll("-", "");
//
//             try {
//                 boolean uuidStringMatcher = id.matches(".*[a-zA-Z]+.*");
//
//                 if (!uuidStringMatcher) {
//                     Random random = new Random();
//                     char cha = (char) (random.nextInt(26) + 'a');
//                     int numToReplace = random.nextInt(9);
//
//                     id = id.replaceAll(String.valueOf(numToReplace), String.valueOf(cha));
//                 }
//             } catch (Exception e) {
////                  logger.info(e.getMessage());
//                 e.printStackTrace();
//             }
//
//             return id;
//         } catch (Exception e) {
////             logger.info(e.getMessage());
//             e.printStackTrace();
//         }
//
//         return "";
//     }

//     public long checkExpiry(LocalDate expiryDate){
//         LocalDate currDate = LocalDate.now();
//         Duration diff = Duration.between(currDate.atStartOfDay(), expiryDate.atStartOfDay());
//         long diffDays = diff.toDays();
//         System.out.println("diffDays: "+diffDays);
//         return diffDays;
//     }

//         public AccessKeyStatusDto checkAccessKeyStatus (String accessKeyDto) {
//             LocalDate currentDate = LocalDate.now();
//             LocalDate expiryDate = accessKeyDto.getExpiryDate();
//
//             boolean isExpired = currentDate.isAfter(expiryDate);
//             String status = isExpired ? "Expired" : "Active";
//
//             return new AccessKeyStatusDto(accessKeyDto.getAccessKey(), status, expiryDate);
//         }


//     public AccessKeyDto revokeAccessKey(Long accessKey) {
//         accessKeyRepository.delete(accessKey);
//     }
//
//     // getting access keys
//     public List<AccessKeyDto> getAllAccessKeys() {
//         List<AccessKey> accessKeys = accessKeyRepository.findAll();
//         List<AccessKeyDto> accessKeyDtos = accessKeys.stream()
//                 .map(accessKey -> mapAccessKeyToDto(accessKey))
//                 .collect(Collectors.toList());
//
//         return accessKeyDtos;
//     }
//
//     private AccessKeyDto mapAccessKeyToDto(AccessKey access) {
//         return new AccessKeyDto(access.getId(),access.getUser().getEmail(),access.getUser().getId(), access.getSchool().getName(), access.getSchool().getId(),
//                 access.getStatus(), access.getDateOfProcurement(), access.getExpiryDate(), access.getAccessKey(),checkExpiry(access.getExpiryDate()));
//     }
//
//     public List<AccessKey> getAccessKeysByUser(AppUser user) {
//
//         return accessKeyRepository.findByUser(user);
//     }

//     public Optional<AccessKey> getAccessKeyById(Long id) {
//
//         return accessKeyRepository.findById(id);
//     }

//     public AccessKey createAccessKey(AccessKeyDto accessKeyDto) {
//         return accessKeyRepository.save(accessKeyDto);
//     }
//     public boolean hasActiveAccessKey(AppUser user) {
//         List<AccessKey> accessKeys = accessKeyRepository.findByUser(user);
//         for (AccessKey accessKey : accessKeys) {
//             if (accessKey.getStatus() == KeyStatus.ACTIVE && accessKey.getExpiryDate().isAfter(LocalDate.now())) {
//                 return true;
//             }
//         }
//         return false;

// public AccessKeyDto updateAccessKey(AccessKeyDto updatedAccessKey) {
//     AccessKey accessKey = accessKeyRepository.findById(updatedAccessKey.id()).get();
//     accessKey.setStatus(updatedAccessKey.status());
//     accessKey.setDateOfProcurement(updatedAccessKey.dateOfProcurement());
//     accessKey.setExpiryDate(updatedAccessKey.expiryDate());
//     accessKey.setAccessKey(genId().toUpperCase());
//     AccessKey access = accessKeyRepository.save(accessKey);
//     return new AccessKeyDto(access.getId(),access.getUser().getEmail(),access.getUser().getId(), access.getSchool().getName(), access.getSchool().getId(),
//             access.getStatus(), access.getDateOfProcurement(), access.getExpiryDate(), access.getAccessKey(),checkExpiry(access.getExpiryDate()));
// }


