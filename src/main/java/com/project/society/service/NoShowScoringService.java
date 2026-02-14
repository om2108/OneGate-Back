package com.project.society.service;

import com.project.society.model.Appointment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class NoShowScoringService {

    public double score(Appointment a) {

        double w1 = 0.30;
        double w2 = 0.20;
        double w3 = 0.15;
        double w4 = 0.20;
        double w5 = 0.15;

        // ---------- NULL SAFE ----------

        int pastNoShows = a.getPastNoShowsCount() == null ? 0 : a.getPastNoShowsCount();
        double percentCancel = a.getPercentCancellations() == null ? 0.0 : a.getPercentCancellations();

        LocalDateTime dateTime =
                a.getDateTime() == null ? LocalDateTime.now() : a.getDateTime();

        String intentStr = a.getIntent() == null ? "" : a.getIntent();
        String propertyStr = a.getPropertyType() == null ? "" : a.getPropertyType();

        // ---------- FEATURES ----------

        double past = Math.min(1.0, pastNoShows / 3.0);

        int hour = dateTime
                .atZone(ZoneId.systemDefault())
                .getHour();

        double isEvening = (hour >= 17 && hour <= 21) ? 1.0 : 0.0;

        double pct = Math.min(1.0, percentCancel);

        double intent = "rent".equalsIgnoreCase(intentStr) ? 1.0 : 0.0;

        double isStudio = "studio".equalsIgnoreCase(propertyStr) ? 1.0 : 0.0;

        // ---------- FINAL SCORE ----------

        double score =
                (w1 * past)
                        + (w2 * isEvening)
                        + (w3 * pct)
                        + (w4 * intent)
                        + (w5 * isStudio);

        return Math.min(1.0, score);
    }
}
