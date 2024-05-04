package com.accesskeymanager.AccessKeyManager.Runners;

import com.accesskeymanager.AccessKeyManager.model.AppUser;
import com.accesskeymanager.AccessKeyManager.model.Role;
import com.accesskeymanager.AccessKeyManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Admin is about to be seeded");
        String email = System.getenv("ADMIN_EMAIL");
        System.out.println(email);
        boolean isAdminExists = userRepository.existsByEmail(email);
        if (!isAdminExists) {
            AppUser user = new AppUser();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(System.getenv("ADMIN_PASSWORD")));
            user.setRole(Role.ADMIN);
            user.setSchool(null);
            user.setVerified(true);
            userRepository.save(user);
        }
    }

}
