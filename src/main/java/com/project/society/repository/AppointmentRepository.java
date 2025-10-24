package com.project.society.repository;

import com.project.society.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment,String> {
    List<Appointment> findByPropertyId(String propertyId);
    List<Appointment> findByUserId(String userId);
}
