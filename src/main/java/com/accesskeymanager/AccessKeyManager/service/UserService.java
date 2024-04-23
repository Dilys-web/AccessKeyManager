package com.accesskeymanager.AccessKeyManager.service;

import com.accesskeymanager.AccessKeyManager.model.Role;
import com.accesskeymanager.AccessKeyManager.model.School;
import com.accesskeymanager.AccessKeyManager.model.AppUser;
import com.accesskeymanager.AccessKeyManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;


    private PasswordEncoder passwordEncoder;

    public Optional<AppUser> authenticate(String email, String password) {
        Optional<AppUser> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            AppUser user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public AppUser register(String email, String password, Role role, School school) {
        AppUser user = new AppUser();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setSchool(school);
        return userRepository.save(user);
    }

    public Optional<AppUser> findByEmail(String email) {

        return userRepository.findByEmail(email);
    }
}
