package com.accesskeymanager.AccessKeyManager.DTO.request;

import lombok.Data;
import lombok.Setter;

@Data
public class CreateSchoolRequest {

    @Setter
    private String name;
    private String emailDomain;

}
