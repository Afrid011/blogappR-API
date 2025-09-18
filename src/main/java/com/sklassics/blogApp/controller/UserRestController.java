package com.sklassics.blogApp.controller;

import com.sklassics.blogApp.entity.User;
import com.sklassics.blogApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // 🔐 Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // ➕ Add Creator
    @PostMapping("/creator")
    public ResponseEntity<?> addCreator(@RequestParam String username,
                                        @RequestParam String password) {
        User created = userService.addCreator(username, password);
        if (created != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
    }

    // 🔄 Update Creator Password
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id,
                                            @RequestParam String newPassword) {
        boolean updated = userService.updateCreatorPassword(id, newPassword);
        if (updated) {
            return ResponseEntity.ok("Password updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Creator not found");
    }

    // 📋 Get All Users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 📋 Get All Creators
    @GetMapping("/creators")
    public ResponseEntity<List<User>> getAllCreators() {
        return ResponseEntity.ok(userService.getAllCreators());
    }
}