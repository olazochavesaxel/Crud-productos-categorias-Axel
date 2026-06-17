package com.project.demo.logic.entity.rol;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Order(3)
@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createDefaultUser();
    }

    private void createDefaultUser() {
        String email = "user@gmail.com";
        String password = "user123";

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        User user = new User();
        user.setName("Default");
        user.setLastname("User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}