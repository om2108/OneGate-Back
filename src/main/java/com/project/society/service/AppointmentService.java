package com.project.society.service;

import com.project.society.model.Appointment;
import com.project.society.model.Notification;
import com.project.society.repository.AppointmentRepository;
import com.project.society.repository.NotificationRepository;
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

    // ✅ FIXED: use instance, not class name
    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    public Appointment requestAppointment(Appointment app) {
        app.setStatus("REQUESTED");
        app.setCreatedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());

        Appointment saved = repo.save(app);

        // Notify owner (optional, depending on logic)
        Notification n = new Notification();
        n.setTargetUserId(app.getPropertyId()); // ownerId should ideally come from property
        n.setMessage("New appointment request for property " + app.getPropertyId());
        n.setReadStatus("UNREAD");
        n.setCreatedAt(LocalDateTime.now());
        n.setUpdatedAt(LocalDateTime.now());
        notificationRepo.save(n);

        return saved;
    }

    public Appointment respondAppointment(String id, boolean accepted, LocalDateTime dateTime, String location) {
        Appointment app = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        app.setStatus(accepted ? "ACCEPTED" : "DECLINED");
        app.setDateTime(dateTime);
        app.setLocation(location);
        app.setOwnerResponse(accepted ? "Accepted" : "Declined");
        app.setUpdatedAt(LocalDateTime.now());

        return repo.save(app);
    }
}
