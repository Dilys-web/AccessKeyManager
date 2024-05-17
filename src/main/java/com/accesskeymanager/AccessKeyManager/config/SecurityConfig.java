package com.accesskeymanager.AccessKeyManager.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    ConfigConstant configConstants = new ConfigConstant();
    private final Environment environment;

    @Value("${FRONT_END_URL}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                        "/api/v1/auth/**", // authorize all the methods that are in this resource"
                                        "/schools/**",
                                        "/api/v1/accesskeys/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/swagger-config",
                                        "/swagger-ui/index.html",
                                        "/v3/api-docs/**",
                                        "/api/v1/docs/**",
                                        "/api/v1/docs.yaml"


                                ).permitAll()
                                .requestMatchers(
                                        "/api/v1/auth/signup",
                                        "/api/v1/auth/reset-password",
                                        "/api/v1/auth/login",
                                        "/api/v1/auth/forgot-password",
                                        "/api/v1/auth/change-password",
                                        "/api/v1/auth/verify-email",
                                        "/api/v1/key/access-keys/request/{schoolId}",
                                        "/api/v1/key/access-keys/all/{userId}",
                                        "/api/v1/key/access-keys/{keyId}"
                                ).hasRole(("SCHOOL_IT_PERSONNEL"))
                                .requestMatchers(
                                        "/api/v1/auth/login",
                                        "/api/v1/accesskeys/all",
                                        "/api/v1/accesskeys/revoke/{keyId}",
                                        "/api/v1/accesskeys/active/{schoolEmail}",
                                        "/schools/get-schools/{id}",
                                        "/schools/create school",
                                        "schools/revoke/{id}"
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

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/api/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedOrigins(frontendUrl)
                        .allowCredentials(true)
                        .allowedHeaders("*");
            }
        };
    }



}