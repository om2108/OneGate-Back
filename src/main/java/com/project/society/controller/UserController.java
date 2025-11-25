// src/main/java/com/project/society/controller/UserController.java
package com.project.society.controller;

import com.project.society.model.Role;
import com.project.society.model.User;
import com.project.society.repository.UserRepository;
import com.project.society.service.EmailService;
import com.project.society.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repo;
    private final UserService userService;
    private final EmailService emailService;

    // 1. OWNER → Invite user
    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(@RequestBody Map<String, String> payload) {

        String email = payload.get("email");
        String role = payload.get("role");

        if (email == null || role == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "email & role required"));
        }

        if (repo.findByEmail(email).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "Email already exists"));
        }

        Role r;
        try {
            r = Role.valueOf(role.toUpperCase());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role"));
        }

        User user = new User();
        user.setEmail(email);
        user.setRole(r);
        user.setPassword(null);
        user.setVerified(false);
        user.setOnboardingCompleted(false);

        repo.save(user);
        emailService.sendInviteLink(email);

        return ResponseEntity.ok(Map.of(
                "message", "Invitation email sent",
                "userId", user.getId()
        ));
    }

    // 2. USER → Complete onboarding
    @PostMapping("/onboarding")
    public ResponseEntity<?> onboarding(@RequestBody Map<String, String> payload) {

        String email = payload.get("email");
        String password = payload.get("password");
        String name = payload.get("name");
        String phone = payload.get("phone");

        if (email == null || password == null || name == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing fields"));
        }

        var opt = repo.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        User u = opt.get();

        if (u.getPassword() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Already onboarded"));
        }

        u.setPassword(userService.encodePassword(password));
        u.setName(name);
        u.setPhone(phone);
        u.setVerified(true);
        u.setOnboardingCompleted(true);

        repo.save(u);

        return ResponseEntity.ok(Map.of("message", "Onboarding completed"));
    }

    // 3. OWNER → Update user role
    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable String id,
                                        @RequestBody Map<String, String> body) {

        String newRole = body.get("role");
        if (newRole == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Role required"));
        }

        return repo.findById(id).map(u -> {
            try {
                u.setRole(Role.valueOf(newRole.toUpperCase()));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid role"));
            }
            repo.save(u);
            return ResponseEntity.ok(Map.of("message", "Role updated"));
        }).orElseGet(() ->
                ResponseEntity.status(404).body(Map.of("error", "User not found"))
        );
    }

    // 4. OWNER → Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }

    // 5. Get all users (protected by SecurityConfig)
    @GetMapping
    public List<User> allUsers() {
        return repo.findAll();
    }

    // 6. Public users list (id + email only)
    @GetMapping("/public")
    public List<Map<String, String>> publicUsers() {
        return repo.findAll().stream()
                .map(u -> Map.of("id", u.getId(), "email", u.getEmail()))
                .toList();
    }
}
