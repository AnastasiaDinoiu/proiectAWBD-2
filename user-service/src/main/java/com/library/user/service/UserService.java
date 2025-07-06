package com.library.user.service;

import com.library.user.entity.User;
import com.library.user.entity.UserDetails;
import com.library.user.exception.ResourceNotFoundException;
import com.library.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<User> findAll(PageRequest pageRequest) {
        log.info("Finding all users with pagination");
        return userRepository.findAll(pageRequest);
    }

    public User findById(Long id) {
        log.info("Finding user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User findByUsername(String username) {
        log.info("Finding user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    public User save(User user) {
        log.info("Saving new user: {}", user.getUsername());

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserDetails userDetails = new UserDetails();
        userDetails.setUser(user);
        user.setUserDetails(userDetails);

        User savedUser = userRepository.save(user);

        log.info("User saved successfully with id: {}", savedUser.getId());
        user.setEmail(user.getEmail());
        user.setEnabled(user.isEnabled());
        user.setRole(user.getRole());

        return userRepository.save(savedUser);
    }

    public User update(Long id, User user) {
        log.info("Updating user with id: {}", id);

        User existingUser = findById(id);
        existingUser.setEmail(user.getEmail());
        existingUser.setEnabled(user.isEnabled());
        existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    public void deleteById(Long id) {
        log.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

    public void changePassword(Long id, String oldPassword, String newPassword) {
        log.info("Changing password for user with id: {}", id);

        User user = findById(id);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
