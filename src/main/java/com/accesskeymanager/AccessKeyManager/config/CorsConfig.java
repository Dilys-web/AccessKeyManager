//package com.accesskeymanager.AccessKeyManager.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@RequiredArgsConstructor
//public class CorsConfig implements WebMvcConfigurer {
//    private final Environment environment;
//    ConfigConstant configConstants = new ConfigConstant();
//
//    /**
//     * Configure CORS mappings for the application.
//     *
//     * @param registry The CORS registry used for configuration.
//     */
////    @Override
////    public void addCorsMappings(CorsRegistry registry) {
////        registry.addMapping("/**")
////                .allowedOrigins(Boolean.TRUE.equals(configConstants.debug) ? "*" : environment.getProperty("ALLOWED_ORIGINS"))
////                .allowedMethods("GET", "POST", "PUT", "DELETE");
////    }
////
////
//        }
////public void addCorsMappings(CorsRegistry registry) {
////                registry
////                        .addMapping("/api/**")
////                        .allowedMethods("GET", "POST", "PUT", "DELETE")
////                        .allowedOrigins("${FRONT_END_URL}")
////                        .allowCredentials(true)
////                        .allowedHeaders("*");
////            }
