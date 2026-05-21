package com.project.society.repository;

import com.project.society.model.Maintenance;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MaintenanceRepository
        extends MongoRepository<Maintenance, String> {

    // =====================================
    // GET ALL BY SOCIETY
    // =====================================

    List<Maintenance> findBySocietyId(
            String societyId
    );

    // =====================================
    // GET BY USER
    // =====================================

    List<Maintenance> findBySocietyIdAndUserId(
            String societyId,
            String userId
    );

    // =====================================
    // CHECK MONTH
    // =====================================

    boolean existsByUserIdAndMonthAndYear(
            String userId,
            Integer month,
            Integer year
    );
}