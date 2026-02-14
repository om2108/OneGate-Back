package com.project.society.repository;

import com.project.society.model.Notice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoticeRepository extends MongoRepository<Notice,String> {
    List<Notice> findBySocietyIdAndTargetRolesIn(String societyId, List<String> roles);
}
