package com.accesskeymanager.AccessKeyManager.config;


public class ConfigConstant {
    public final boolean debug;


    public ConfigConstant() {
        this.debug = Boolean.parseBoolean(System.getenv("DEBUG"));
    }
}
