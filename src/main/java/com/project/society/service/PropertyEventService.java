package com.project.society.service;

import com.project.society.model.Property;
import com.project.society.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyEventService {

    private final PropertyRepository propertyRepository;

    /**
     * Increase property view/click count by 1 each time a user opens a property.
     */
    public void recordClick(String propertyId) {
        propertyRepository.findById(propertyId).ifPresent(p -> {
            Integer current = p.getViews();
            if (current == null) current = 0;
            p.setViews(current + 1);
            propertyRepository.save(p);
        });
    }
}
