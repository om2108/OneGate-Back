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
    ){

        this.repo=repo;

    }


    public VisitorEntry addVisitor(
            VisitorEntry v
    ){

        v.setApprovalLevel(
                "SECRETARY"
        );

        v.setSecretaryDecision(
                "PENDING"
        );

        v.setMemberDecision(
                "PENDING"
        );

        v.setStatus(
                "PENDING"
        );

        v.setImageVerified(
                true
        );

        v.setCreatedAt(
                LocalDateTime.now()
        );

        v.setUpdatedAt(
                LocalDateTime.now()
        );

        return repo.save(v);

    }


    public List<VisitorEntry>
    secretaryQueue(
            String societyId
    ){

        return repo
                .findBySocietyIdAndApprovalLevel(
                        societyId,
                        "SECRETARY"
                );

    }


    public List<VisitorEntry>
    memberQueue(
            String societyId,
            String memberId
    ){

        return repo
                .findBySocietyIdAndMemberId(
                        societyId,
                        memberId
                );

    }


    public VisitorEntry approve(
            String id,
            String role
    ){

        VisitorEntry v =
                repo
                        .findById(id)
                        .orElseThrow();

        if(
                "SECRETARY"
                        .equals(role)
        ){

            v.setSecretaryDecision(
                    "APPROVED"
            );

        }

        if(
                "MEMBER"
                        .equals(role)
        ){

            v.setMemberDecision(
                    "APPROVED"
            );

        }

        v.setStatus(
                "APPROVED"
        );

        v.setCheckIn(
                LocalDateTime.now()
        );

        return repo.save(v);

    }

    public VisitorEntry reject(
            String id,
            String role
    ){

        VisitorEntry v =
                repo
                        .findById(id)
                        .orElseThrow();

        if(
                "SECRETARY"
                        .equals(role)
        ){

            v.setSecretaryDecision(
                    "REJECTED"
            );

        }

        if(
                "MEMBER"
                        .equals(role)
        ){

            v.setMemberDecision(
                    "REJECTED"
            );

        }

        v.setStatus(
                "REJECTED"
        );

        return repo.save(v);

    }

    public VisitorEntry forward(
            String id,
            String memberId
    ){

        VisitorEntry v =
                repo
                        .findById(id)
                        .orElseThrow();

        v.setApprovalLevel(
                "MEMBER"
        );

        v.setSecretaryDecision(
                "FORWARDED"
        );

        v.setMemberId(
                memberId
        );

        return repo.save(v);

    }

}