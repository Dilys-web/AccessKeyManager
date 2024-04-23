package com.accesskeymanager.AccessKeyManager.DTO;

import lombok.Setter;

public class CreateSchoolRequest {
    public String getName() {
        return name;
    }

    @Setter
    private String name;
    private String emailDomain;

    public String getEmailDomain() {
        return emailDomain;
    }
}
