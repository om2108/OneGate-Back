package com.project.society.repository;

import com.project.society.model.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ComplaintRepository extends MongoRepository<Complaint,String> {

    List<Complaint> findBySocietyId(String societyId);

    List<Complaint> findByCreatedBy(String userId);
}
