package com.sklassics.blogApp.service;

import org.springframework.stereotype.Service;

import com.sklassics.blogApp.entity.User;
import com.sklassics.blogApp.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Auto-create admin
    @PostConstruct
    public void initAdmin() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", "admin123", "admin");
            userRepository.save(admin);
        }
    }

    // ✅ login (plain text)
    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && password.equals(userOpt.get().getPassword())) {
            return userOpt.get();
        }
        return null;
    }

    // ✅ find by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // ✅ save user (plain text)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // ✅ get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ get all creators
    public List<User> getAllCreators() {
        return userRepository.findAll()
                .stream()
                .filter(u -> "creator".equals(u.getRole()))
                .collect(Collectors.toList());
    }

    // ✅ add new creator with duplicate check
    public User addCreator(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return null;
        }
        User user = new User(username, password, "creator");
        return userRepository.save(user);
    }

    // ✅ update creator password
    public boolean updateCreatorPassword(Long creatorId, String newPassword) {
        Optional<User> userOpt = userRepository.findById(creatorId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
