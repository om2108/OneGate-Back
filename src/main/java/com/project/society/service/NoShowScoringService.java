package com.project.society.service;

import com.project.society.model.Appointment;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class NoShowScoringService {

    public double score(Appointment a) {

        double w1 = 0.30; // past no-shows
        double w2 = 0.20; // time of appointment
        double w3 = 0.15; // percent cancellations
        double w4 = 0.20; // intent
        double w5 = 0.15; // property type

        double past = Math.min(1.0, (a.getPastNoShowsCount() / 3.0));
        int hour = a.getDateTime()
                .atZone(ZoneId.systemDefault())
                .getHour();
        double isEvening = (hour >= 17 && hour <= 21) ? 1.0 : 0.0;
        double pct = Math.min(1.0, a.getPercentCancellations());
        double intent = "rent".equalsIgnoreCase(a.getIntent()) ? 1.0 : 0.0;
        double isStudio = "studio".equalsIgnoreCase(a.getPropertyType()) ? 1.0 : 0.0;

        double score = w1 * past + w2 * isEvening + w3 * pct + w4 * intent + w5 * isStudio;
        return Math.min(1.0, score);
    }
}

