 package com.accesskeymanager.AccessKeyManager.service;

 import com.accesskeymanager.AccessKeyManager.DTO.request.AccessKeyDto;
 import com.accesskeymanager.AccessKeyManager.DTO.request.AccessKeyStatusDto;
 import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;
 import com.accesskeymanager.AccessKeyManager.Exception.AccessKeyNotFoundException;
 import com.accesskeymanager.AccessKeyManager.model.AccessKey;
 import com.accesskeymanager.AccessKeyManager.model.AppUser;
 import com.accesskeymanager.AccessKeyManager.model.School;
 import com.accesskeymanager.AccessKeyManager.repository.AccessKeyRepository;
 import com.accesskeymanager.AccessKeyManager.repository.SchoolRepository;
 import com.accesskeymanager.AccessKeyManager.repository.UserRepository;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 import java.time.LocalDate;
 import java.util.List;
 import java.util.Optional;
 import java.util.Random;
 import java.util.UUID;
 import java.util.stream.Collectors;

 @Service
 public class AccessKeyService {
     @Autowired
     private AccessKeyRepository accessKeyRepository;
     @Autowired
     private UserRepository userRepository;
      @Autowired
      private SchoolRepository schoolRepository;
     public AccessKeyDto generateAccessKey(AccessKeyDto accessKeyDto) {
         Optional<AppUser> user = userRepository.findById(accessKeyDto.userId(accessKey.getId()));
         Optional<School> school = schoolRepository.findById(accessKeyDto.schoolId());
         AccessKeyDto dto = null;
         AccessKey accessKey = new AccessKey();
         accessKey.setUser(user.orElse(null));
         accessKey.setSchool(school.orElse(null));

         accessKey.setStatus(accessKeyDto.status());
         accessKey.setDateOfProcurement(accessKeyDto.dateOfProcurement());
         accessKey.setExpiryDate(accessKeyDto.expiryDate(accessKey.getExpiryDate()));
         accessKey.setAccessKey(genId().toUpperCase());
         AccessKey access = accessKeyRepository.save(accessKey);
         if(access!=null){
             dto = new AccessKeyDto(access.getId(),access.getUser().getEmail(),access.getUser().getId(), access.getSchool().getName(), access.getSchool().getId(),
                     access.getStatus(), access.getDateOfProcurement(), access.getExpiryDate(), access.getAccessKey());
         }
         return dto;
     }
     public String genId() {
         try {
             String id = UUID.randomUUID().toString().replaceAll("-", "");

             try {
                 boolean uuidStringMatcher = id.matches(".*[a-zA-Z]+.*");

                 if (uuidStringMatcher == false) {
                     Random random = new Random();
                     char cha = (char) (random.nextInt(26) + 'a');
                     int numToReplace = random.nextInt(9);

                     id = id.replaceAll(String.valueOf(numToReplace), String.valueOf(cha));
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }

             return id;
         } catch (Exception e) {
             e.printStackTrace();
         }

         return "";
     }


//         public  AccessKeyStatusDto checkAccessKeyStatus (String accessKeyDto) {
//             LocalDate currentDate = LocalDate.now();
//             LocalDate expiryDate = accessKeyDto.getExpiryDate();
//
//             boolean isExpired = currentDate.isAfter(expiryDate);
//             String status = isExpired ? "Expired" : "Active";
//
//             return new AccessKeyStatusDto(accessKeyDto.getAccessKey(), status, expiryDate);
//         }


     public void revokeAccessKey(AccessKey accessKey) {
             Optional<AccessKey> optionalAccessKey = accessKeyRepository.findById(accessKey.getId());

             if (optionalAccessKey.isPresent()) {
                 AccessKey keyToRevoke = optionalAccessKey.get();
                 accessKeyRepository.delete(keyToRevoke);
             } else {
                 throw new AccessKeyNotFoundException("Access key not found");
             }
         }



     // getting access keys
     public List<AccessKeyDto> getAllAccessKeysDto() {
         List<AccessKey> accessKeys = accessKeyRepository.findAll();

         List<AccessKeyDto> accessKeyDtos = accessKeys.stream()
                 .map(accessKey -> mapAccessKeyToDto(accessKey))
                 .collect(Collectors.toList());

         return accessKeyDtos;
     }

     private AccessKeyDto mapAccessKeyToDto(AccessKey accessKey) {
         AccessKeyDto accessKeyDto = new AccessKeyDto();
         accessKeyDto.userId(accessKey.getId());
         accessKeyDto.status(accessKey.getStatus());
         accessKeyDto.expiryDate(accessKey.getExpiryDate());
         // Map other properties as needed
         return accessKeyDto;
     }

     public List<AccessKey> getAccessKeysByUser(AppUser user) {

         return accessKeyRepository.findByUser(user);
     }

     public Optional<AccessKey> getAccessKeyById(Long id) {

         return accessKeyRepository.findById(id);
     }

//     public AccessKey createAccessKey(AccessKeyDto accessKeyDto) {
//         return accessKeyRepository.save(accessKeyDto);
//     }
     public boolean hasActiveAccessKey(AppUser user) {
         List<AccessKey> accessKeys = accessKeyRepository.findByUser(user);
         for (AccessKey accessKey : accessKeys) {
             if (accessKey.getStatus() == KeyStatus.ACTIVE && accessKey.getExpiryDate().isAfter(LocalDate.now())) {
                 return true;
             }
         }
         return false;
     }

 public AccessKey updateAccessKey(AccessKey updatedAccessKey) {
     Optional<AccessKey> existingAccessKeyOptional = accessKeyRepository.findById(updatedAccessKey.getId());

     if (existingAccessKeyOptional.isPresent()) {
         AccessKey existingAccessKey = existingAccessKeyOptional.get();

         existingAccessKey.setUser(updatedAccessKey.getUser());
         existingAccessKey.setSchool(updatedAccessKey.getSchool());
         existingAccessKey.setStatus(updatedAccessKey.getStatus());
         existingAccessKey.setDateOfProcurement(updatedAccessKey.getDateOfProcurement());
         existingAccessKey.setExpiryDate(updatedAccessKey.getExpiryDate());

         return accessKeyRepository.save(existingAccessKey);
     } else {
         // Handle the case where the access key with the provided ID is not found
         return null;
     }

//     public List<AccessKey> getAllAccessKeys() {
//
//         return accessKeyRepository.findAll();
//     }
 }

 ///key expiring

//     public static LocalDate _7DaysFromToday() {
//         return LocalDate.now().plusDays(7);
//     }
//     public static LocalDate _7DaysBeforeToday() {
//         return LocalDate.now().minusDays(7);
//     }
//     public static LocalDate nextWeekDate() {
//         return LocalDate.now().plusDays(LocalDate.now().getDayOfWeek().getValue());
//     }
//     public static LocalDate previousWeekDate() {
//         return LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue());
//     }
 }

