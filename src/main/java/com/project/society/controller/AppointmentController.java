package com.project.society.controller;

import com.project.society.model.Appointment;
import com.project.society.model.User;
import com.project.society.service.AppointmentService;
import com.project.society.service.ProfileService;
import com.project.society.model.Profile;
import com.project.society.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return service.getAllAppointments();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        String email = authentication.getDetails().toString();

        return userService.findByEmail(email)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

    @PostMapping
    public ResponseEntity<?> request(@RequestBody Appointment app,
                                     Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Not authenticated"));
        }

        String email = authentication.getDetails().toString();

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email " + email));

        // 🔒 PROFILE COMPLETION CHECK
        Profile profile = profileService.getProfile(email);

        if (!profile.isProfileComplete()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Complete your profile before requesting appointment"));
        }

        // always trust backend, not frontend
        app.setUserId(user.getId());

        Appointment saved = service.requestAppointment(app);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}/respond")
    public Appointment respond(@PathVariable String id,
                               @RequestParam boolean accepted,
                               @RequestParam(required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                               LocalDateTime dateTime,
                               @RequestParam(required = false) String location) {

        return service.respondAppointment(id, accepted, dateTime, location);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        service.deleteAppointment(id);
        return "Appointment deleted";
    }
}
