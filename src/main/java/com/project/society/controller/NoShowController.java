package com.project.society.controller;

import com.project.society.model.Appointment;
import com.project.society.repository.AppointmentRepository;
import com.project.society.service.NoShowScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/no-show")
@RequiredArgsConstructor
public class NoShowController {

    private final AppointmentRepository repo;
    private final NoShowScoringService scoringService;

    @PostMapping("/score/{id}")
    public Appointment scoreAppointment(@PathVariable String id) {
        Appointment a = repo.findById(id).orElseThrow();
        double p = scoringService.score(a);

        a.setNoShowScore(p);
        a.setNoShowFlag(p >= 0.7);
        a.setLastScoredAt(new Date());

        return repo.save(a);
    }
}

