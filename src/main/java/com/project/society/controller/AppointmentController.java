package com.project.society.controller;

import com.project.society.model.Appointment;
import com.project.society.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService service;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return service.getAllAppointments();
    }

    @PostMapping
    public Appointment request(@RequestBody Appointment app){ return service.requestAppointment(app); }

    @PutMapping("/{id}/respond")
    public Appointment respond(@PathVariable String id,
                               @RequestParam boolean accepted,
                               @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                               @RequestParam(required=false) String location){
        return service.respondAppointment(id, accepted, dateTime, location);
    }
}
