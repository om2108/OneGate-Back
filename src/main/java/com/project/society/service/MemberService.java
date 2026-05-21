// ============================================
// MemberService.java
// UPDATED
// ============================================

package com.project.society.service;

import com.project.society.model.Member;
import com.project.society.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository repo;

    // =====================================
    // GET MEMBERS BY SOCIETY
    // =====================================

    public List<Member> getMembersBySociety(
            String societyId
    ) {

        return repo.findBySocietyId(
                societyId
        );
    }

    // =====================================
    // GET MEMBER BY USER ID
    // =====================================

    public Member getByUserId(
            String userId
    ){

        return repo.findFirstByUserId(
                userId
        ).orElseThrow(

                () -> new RuntimeException(
                        "Member not found"
                )
        );
    }

    // =====================================
    // ADD MEMBER
    // =====================================

    public Member addMember(
            Member member
    ) {

        member.setJoinedAt(
                LocalDateTime.now()
        );

        return repo.save(member);
    }

    // =====================================
    // UPDATE ROLE + PROPERTY
    // =====================================

    public Member updateRole(
            String memberId,
            String role,
            String propertyId
    ) {

        Member member =
                repo.findById(memberId)

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Member not found"
                                )
                        );

        member.setRole(role);

        // SAVE PROPERTY

        member.setPropertyId(
                propertyId
        );

        return repo.save(member);
    }

    // =====================================
    // DELETE MEMBER
    // =====================================

    public void deleteMember(
            String memberId
    ) {

        repo.deleteById(
                memberId
        );
    }
}