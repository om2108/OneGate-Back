package com.project.society.service;

import com.project.society.model.Appointment;
import com.project.society.model.Property;
import com.project.society.repository.AppointmentRepository;
import com.project.society.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repo;
    private final PropertyRepository propertyRepository;
    private final NotificationService notificationService;

    // ---------------- GET ALL ----------------
    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    // ---------------- REQUEST ----------------
    public Appointment requestAppointment(Appointment app) {

        Property property = propertyRepository.findById(app.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        app.setStatus("REQUESTED");
        app.setCreatedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());

        Appointment saved = repo.save(app);

        String requesterId = saved.getUserId();   // USER / MEMBER
        String ownerId = property.getOwnerId();   // OWNER

        // ================= REQUESTER =================
        if (requesterId != null) {
            notificationService.create(
                    requesterId,
                    "✅ Your visit request has been sent successfully."
            );
        }

        // ================= OWNER =================
        if (ownerId != null) {
            notificationService.create(
                    ownerId,
                    "📩 New appointment request for property: " + property.getName()
            );
        }

        return saved;
    }

    // ---------------- OWNER RESPONSE ----------------
    public Appointment respondAppointment(String id,
                                          boolean accepted,
                                          LocalDateTime dateTime,
                                          String location) {

        Appointment app = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        app.setStatus(accepted ? "ACCEPTED" : "DECLINED");
        app.setDateTime(dateTime);
        app.setLocation(location);
        app.setOwnerResponse(accepted ? "Accepted" : "Declined");
        app.setUpdatedAt(LocalDateTime.now());

        Appointment updated = repo.save(app);

        String tenantId = app.getUserId();   // USER / MEMBER

        Property property = propertyRepository
                .findById(app.getPropertyId())
                .orElse(null);

        String ownerId = property != null ? property.getOwnerId() : null;

        // ================= TENANT / MEMBER =================
        if (tenantId != null) {
            notificationService.create(
                    tenantId,
                    accepted
                            ? "✅ Your appointment was ACCEPTED."
                            : "❌ Your appointment was DECLINED."
            );
        }

        // ================= OWNER =================
        if (ownerId != null) {
            notificationService.create(
                    ownerId,
                    accepted
                            ? "✅ You accepted an appointment request."
                            : "❌ You declined an appointment request."
            );
        }

        return updated;
    }

    // ---------------- DELETE ----------------
    public void deleteAppointment(String id) {
        repo.deleteById(id);
    }
}

