// src/main/java/com/project/society/controller/UserController.java

package com.project.society.controller;

import com.project.society.model.Member;
import com.project.society.model.Property;
import com.project.society.model.Role;
import com.project.society.model.User;

import com.project.society.repository.MemberRepository;
import com.project.society.repository.PropertyRepository;
import com.project.society.repository.UserRepository;

import com.project.society.service.EmailService;
import com.project.society.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    // ✅ ADD THESE
    private final MemberRepository memberRepository;

    private final PropertyRepository propertyRepository;

    // 1. OWNER → Invite user
    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(
            @RequestBody Map<String, String> payload
    ) {

        String email = payload.get("email");

        String role = payload.get("role");

        if (email == null || role == null) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "email & role required"
                            )
                    );
        }

        if (repo.findByEmail(email).isPresent()) {

            return ResponseEntity.status(409)
                    .body(
                            Map.of(
                                    "error",
                                    "Email already exists"
                            )
                    );
        }

        Role r;

        try {

            r = Role.valueOf(
                    role.toUpperCase()
            );

        } catch (Exception ex) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Invalid role"
                            )
                    );
        }

        User user = new User();

        user.setEmail(email);

        user.setRole(r);

        user.setPassword(null);

        user.setVerified(false);

        user.setOnboardingCompleted(false);

        repo.save(user);

        emailService.sendInviteLink(email);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Invitation email sent",

                        "userId",
                        user.getId()
                )
        );
    }

    // 2. USER → Complete onboarding
    @PostMapping("/onboarding")
    public ResponseEntity<?> onboarding(
            @RequestBody Map<String, String> payload
    ) {

        String email =
                payload.get("email");

        String password =
                payload.get("password");

        String name =
                payload.get("name");

        String phone =
                payload.get("phone");

        if (
                email == null ||
                        password == null ||
                        name == null
        ) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Missing fields"
                            )
                    );
        }

        var opt =
                repo.findByEmail(email);

        if (opt.isEmpty()) {

            return ResponseEntity.status(404)
                    .body(
                            Map.of(
                                    "error",
                                    "User not found"
                            )
                    );
        }

        User u = opt.get();

        if (u.getPassword() != null) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Already onboarded"
                            )
                    );
        }

        u.setPassword(
                userService.encodePassword(password)
        );

        u.setName(name);

        u.setPhone(phone);

        u.setVerified(true);

        u.setOnboardingCompleted(true);

        repo.save(u);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Onboarding completed"
                )
        );
    }


    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(
            @PathVariable String id,

            @RequestBody Map<String, String> body
    ) {

        String newRole =
                body.get("role");

        String propertyId =
                body.get("propertyId");

        return repo.findById(id).map(user -> {

            try {

                // ✅ UPDATE USER ROLE
                user.setRole(
                        Role.valueOf(
                                newRole.toUpperCase()
                        )
                );

                repo.save(user);

                System.out.println(
                        "USER ROLE UPDATED"
                );

                // ✅ CREATE / UPDATE MEMBER
                if (
                        user.getRole() == Role.MEMBER ||
                                user.getRole() == Role.SECRETARY ||
                                user.getRole() == Role.WATCHMAN
                ) {

                    Member member =
                            memberRepository
                                    .findFirstByUserId(
                                            user.getId()
                                    )
                                    .orElse(
                                            new Member()
                                    );

                    System.out.println(
                            "MEMBER BEFORE SAVE: "
                                    + member
                    );

                    // ✅ IMPORTANT
                    member.setUserId(
                            user.getId()
                    );

                    member.setRole(
                            user.getRole().name()
                    );

                    member.setPropertyId(
                            propertyId
                    );

                    // ✅ SET SOCIETY FROM PROPERTY
                    if (
                            propertyId != null &&
                                    !propertyId.isEmpty()
                    ) {

                        Property property =
                                propertyRepository
                                        .findById(
                                                propertyId
                                        )
                                        .orElse(null);

                        if (property != null) {

                            member.setSocietyId(
                                    property.getSocietyId()
                            );

                            System.out.println(
                                    "FOUND PROPERTY: "
                                            + property.getName()
                            );

                            System.out.println(
                                    "FOUND SOCIETY: "
                                            + property.getSocietyId()
                            );
                        }
                    }

                    // ✅ JOINED TIME
                    if (
                            member.getJoinedAt()
                                    == null
                    ) {

                        member.setJoinedAt(
                                java.time.LocalDateTime.now()
                        );
                    }

                    // ✅ SAVE MEMBER
                    Member savedMember =
                            memberRepository.save(
                                    member
                            );

                    System.out.println(
                            "MEMBER SAVED SUCCESSFULLY"
                    );

                    System.out.println(
                            savedMember
                    );
                }

                return ResponseEntity.ok(
                        Map.of(
                                "message",
                                "Role updated successfully"
                        )
                );

            } catch (Exception e) {

                e.printStackTrace();

                return ResponseEntity.badRequest()
                        .body(
                                Map.of(
                                        "error",
                                        e.getMessage()
                                )
                        );
            }

        }).orElseGet(() ->

                ResponseEntity.status(404)
                        .body(
                                Map.of(
                                        "error",
                                        "User not found"
                                )
                        )
        );
    }

    // 4. OWNER → Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable String id
    ) {

        if (!repo.existsById(id)) {

            return ResponseEntity.status(404)
                    .body(
                            Map.of(
                                    "error",
                                    "User not found"
                            )
                    );
        }

        repo.deleteById(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "User deleted"
                )
        );
    }

    // ✅ UPDATED USERS API
    @GetMapping
    public List<Map<String, Object>> allUsers() {

        List<User> users =
                repo.findAll();

        return users.stream().map(user -> {

            Map<String, Object> dto =
                    new HashMap<>();

            dto.put("id", user.getId());

            dto.put("name", user.getName());

            dto.put("email", user.getEmail());

            dto.put("role", user.getRole());

            dto.put("verified", user.isVerified());

            // ✅ FIND MEMBER
            Member member =
                    memberRepository
                            .findFirstByUserId(
                                    user.getId()
                            )
                            .orElse(null);

            if (member != null) {

                dto.put(
                        "propertyId",
                        member.getPropertyId()
                );

                dto.put(
                        "societyId",
                        member.getSocietyId()
                );

                // ✅ FIND PROPERTY
                if (
                        member.getPropertyId()
                                != null
                ) {

                    Property property =
                            propertyRepository
                                    .findById(
                                            member.getPropertyId()
                                    )
                                    .orElse(null);

                    if (property != null) {

                        dto.put(
                                "propertyName",
                                property.getName()
                        );
                    }
                }
            }

            return dto;

        }).toList();
    }

    // 6. Public users list
    @GetMapping("/public")
    public List<Map<String, String>> publicUsers() {

        return repo.findAll()
                .stream()
                .map(u ->
                        Map.of(
                                "id",
                                u.getId(),

                                "email",
                                u.getEmail()
                        )
                ).toList();
    }

    // 7. Get single user by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable String id
    ) {

        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound()
                                .build()
                );
    }
}