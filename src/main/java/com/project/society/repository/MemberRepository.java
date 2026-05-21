// ============================================
// MemberRepository.java
// UPDATED
// ============================================

package com.project.society.repository;

import com.project.society.model.Member;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository
        extends MongoRepository<Member, String> {

    // =====================================
    // GET MEMBERS BY SOCIETY
    // =====================================

    List<Member> findBySocietyId(
            String societyId
    );

    // =====================================
    // GET MEMBERS BY USER ID
    // =====================================

    List<Member> findByUserId(
            String userId
    );

    // =====================================
    // GET FIRST MEMBER BY USER ID
    // =====================================

    Optional<Member> findFirstByUserId(
            String userId
    );
}