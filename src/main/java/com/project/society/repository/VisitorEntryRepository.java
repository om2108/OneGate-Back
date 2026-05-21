package com.project.society.repository;

import com.project.society.model.VisitorEntry;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VisitorEntryRepository
        extends MongoRepository<VisitorEntry, String>{

    List<VisitorEntry>
    findBySocietyId(
            String societyId
    );


    List<VisitorEntry>
    findBySocietyIdAndApprovalLevel(
            String societyId,
            String approvalLevel
    );


    List<VisitorEntry>
    findBySocietyIdAndMemberId(
            String societyId,
            String memberId
    );

}