package com.project.society.service;

import com.project.society.model.VisitorEntry;
import com.project.society.repository.VisitorEntryRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

@Service
public class VisitorEntryService {

    private final VisitorEntryRepository repo;

    public VisitorEntryService(
            VisitorEntryRepository repo
    ) {
        this.repo = repo;
    }

    public VisitorEntry addVisitor(
            VisitorEntry v
    ) {

        v.setApprovalLevel(
                "SECRETARY"
        );

        v.setSecretaryDecision(
                "PENDING"
        );

        v.setStatus(
                "PENDING"
        );

        v.setImageVerified(
                v.getImageUrl() != null
        );

        v.setCreatedBy(
                "WATCHMAN"
        );

        v.setActive(
                false
        );

        v.setCreatedAt(
                LocalDateTime.now()
        );

        v.setUpdatedAt(
                LocalDateTime.now()
        );

        return repo.save(v);
    }

    public VisitorEntry createRegularVisitor(
            VisitorEntry v
    ) {

        v.setVisitorCategory(
                "REGULAR"
        );

        v.setCreatedBy(
                "SECRETARY"
        );

        v.setSecretaryDecision(
                "APPROVED"
        );

        v.setApprovalLevel(
                "DONE"
        );

        v.setStatus(
                "APPROVED"
        );

        v.setImageVerified(
                true
        );

        v.setActive(
                true
        );

        v.setApprovedAt(
                LocalDateTime.now()
        );

        v.setCreatedAt(
                LocalDateTime.now()
        );

        return repo.save(v);
    }

    public List<VisitorEntry>
    secretaryQueue(
            String societyId
    ) {

        return repo
                .findBySocietyIdAndApprovalLevel(
                        societyId,
                        "SECRETARY"
                );
    }

    public VisitorEntry approve(
            String id
    ) {

        VisitorEntry v =
                repo
                        .findById(id)
                        .orElseThrow();

        v.setSecretaryDecision(
                "APPROVED"
        );

        v.setStatus(
                "APPROVED"
        );

        v.setActive(
                true
        );

        v.setApprovedAt(
                LocalDateTime.now()
        );

        v.setUpdatedAt(
                LocalDateTime.now()
        );

        return repo.save(v);
    }

    public VisitorEntry reject(
            String id
    ) {

        VisitorEntry v =
                repo
                        .findById(id)
                        .orElseThrow();

        v.setSecretaryDecision(
                "REJECTED"
        );

        v.setStatus(
                "REJECTED"
        );

        v.setActive(
                false
        );

        v.setUpdatedAt(
                LocalDateTime.now()
        );

        return repo.save(v);
    }

    public VisitorEntry checkIn(
            String id
    ) {

        VisitorEntry v =
                repo
                        .findById(id)
                        .orElseThrow();

        v.setCheckIn(
                LocalDateTime.now()
        );

        v.setLastVisit(
                LocalDateTime.now()
        );

        return repo.save(v);
    }

    public VisitorEntry checkOut(
            String id
    ) {

        VisitorEntry v =
                repo
                        .findById(id)
                        .orElseThrow();

        v.setCheckOut(
                LocalDateTime.now()
        );

        v.setStatus(
                "CHECKED_OUT"
        );

        return repo.save(v);
    }

    public List<VisitorEntry>
    all(
            String societyId
    ) {

        return repo
                .findBySocietyId(
                        societyId
                );
    }

}