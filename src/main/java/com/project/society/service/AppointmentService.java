package com.project.society.service;

import com.project.society.model.Appointment;
import com.project.society.model.Notification;
import com.project.society.model.Property;
import com.project.society.repository.AppointmentRepository;
import com.project.society.repository.NotificationRepository;
import com.project.society.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository repo;

    @Autowired
    private NotificationRepository notificationRepo;

    @Autowired
    private PropertyRepository propertyRepository;

    // ---------------- GET ALL ----------------
    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    // ---------------- REQUEST APPOINTMENT ----------------
    public Appointment requestAppointment(Appointment app) {

        Property property = propertyRepository.findById(app.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        app.setStatus("REQUESTED");
        app.setCreatedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());

        Appointment saved = repo.save(app);

        String tenantId = saved.getTenantId();

        // 🔥 OWNER FIX
        String ownerId = property.getOwnerId();

        // fallback hardcoded owner
        if (ownerId == null || ownerId.isBlank()) {
            ownerId = "6900e550167823154c5b7492";
        }

        System.out.println("TENANT ID = " + tenantId);
        System.out.println("OWNER ID = " + ownerId);

        // ---------------- TENANT NOTIFICATION ----------------
        if (tenantId != null) {
            Notification tenantNotif = new Notification(
                    null,
                    "✅ Your visit request has been sent successfully.",
                    tenantId,
                    "UNREAD",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            notificationRepo.save(tenantNotif);
        }

        // ---------------- OWNER NOTIFICATION ----------------
        if (ownerId != null) {
            Notification ownerNotif = new Notification(
                    null,
                    "📩 New appointment request for property: " + property.getName(),
                    ownerId,
                    "UNREAD",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            notificationRepo.save(ownerNotif);
        }

        return saved;
    }


    // ---------------- OWNER RESPONSE ----------------
    public Appointment respondAppointment(String id, boolean accepted, LocalDateTime dateTime, String location) {

        Appointment app = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        app.setStatus(accepted ? "ACCEPTED" : "DECLINED");
        app.setDateTime(dateTime);
        app.setLocation(location);
        app.setOwnerResponse(accepted ? "Accepted" : "Declined");
        app.setUpdatedAt(LocalDateTime.now());

        Appointment updated = repo.save(app);

        // ✅ FIXED
        String tenantId = app.getUserId();   // frontend value directly

        if (tenantId != null) {
            Notification responseNotif = new Notification(
                    null,
                    accepted
                            ? "✅ Your appointment has been accepted."
                            : "❌ Your appointment was declined.",
                    tenantId,
                    "UNREAD",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            notificationRepo.save(responseNotif);
        }

        return updated;
    }

    // ---------------- DELETE ----------------
    public void deleteAppointment(String id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Appointment not found");
        }
        repo.deleteById(id);
    }
}
