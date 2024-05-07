package com.accesskeymanager.AccessKeyManager.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    ConfigConstant configConstants = new ConfigConstant();
    private final Environment environment;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                        "api/v1/auth/**", // authorize all the methods that are in this resource"
                                        "/schools/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/swagger-config",
                                        "/swagger-ui/index.html",
                                        "/v3/api-docs/**",
                                        "/api/v1/docs/**",
                                        "/api/v1/docs.yaml"
//                                        "api/v1/key/access-keys/request/{schoolId}"


                                ).permitAll()
                                .requestMatchers(
                                        "api/v1/auth/signup",
                                        "api/v1/auth/reset-password",
                                        "api/v1/auth/login",
                                        "api/v1/auth/forgot-password",
                                        "api/v1/auth/change-password",
                                        "api/v1/auth/verify-email",
                                        "api/v1/key/access-keys/request/{schoolId}",
                                        "api/v1/key/access-keys/request/{userId}"
                                ).hasRole(("SCHOOL_IT_PERSONNEL"))
                                .requestMatchers(
                                        "api/v1/auth/login",
                                        "api/v1/accesskeys/all",
                                        "api/v1/accesskeys/revoke",
                                        "api/v1/accesskeys/active/{schoolEmail}"
                                ).hasRole(("ADMIN"))
                                .anyRequest()
                                .authenticated()
                )
                //the '"STATELESS"' means that, spring should not store the session state in its context
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        config.addAllowedOrigin(Boolean.TRUE.equals(configConstants.debug) ? "*" : environment.getProperty("ALLOWED_ORIGINS"));
//        config.setAllowCredentials(true);
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }


}