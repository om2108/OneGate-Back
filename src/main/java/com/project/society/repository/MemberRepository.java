package com.project.society.repository;

import com.project.society.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MemberRepository extends MongoRepository<Member,String> {
    List<Member> findBySocietyId(String societyId);
    List<Member> findByUserId(String userId);
}
