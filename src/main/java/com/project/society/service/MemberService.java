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

    public List<Member> getMembersBySociety(String societyId){
        return repo.findBySocietyId(societyId);
    }

    public Member addMember(Member member){
        member.setJoinedAt(LocalDateTime.now());
        return repo.save(member);
    }

    public Member updateRole(String memberId, String role){
        Member m = repo.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        m.setRole(role);
        return repo.save(m);
    }

    public void deleteMember(String memberId){ repo.deleteById(memberId); }
}

