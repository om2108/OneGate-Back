package com.project.society.service;

import com.project.society.dto.RecommendRequest;
import com.project.society.model.Property;
import com.project.society.repository.PropertyRepository;
import com.project.society.repository.UserRepository;
import com.project.society.util.VectorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Simple recommendation service:
 * - For authenticated users: compute user vector (placeholder) and rank properties by cosine similarity.
 * - For anonymous: rank by simple heuristics (e.g., price closeness, popularity).
 *
 * This is intentionally simple and easy to extend.
 */
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    /**
     * Personalized recommendations for a given user (email).
     */
    public List<Property> recommendForUser(String email, RecommendRequest req) {
        // load properties
        List<Property> all = propertyRepository.findAll();

        // build user vector - placeholder: if you store user interest vector in DB use it here
        double[] userVector = buildUserVectorFromHistoryOrProfile(email, req);

        // compute property vectors (example: price + dummy features)
        Map<Property, Double> score = new HashMap<>();
        for (Property p : all) {
            double[] v = toVector(p);
            double sim = VectorUtil.cosineSimilarity(userVector, v);
            score.put(p, sim);
        }

        // sort descending by score and apply filters
        return applyFiltersAndPick(score, req, Math.max(1, req.getK()));
    }

    /**
     * Generic recommendations (no user) — simple heuristic:
     * - If req.minPrice/maxPrice provided filter.
     * - Then sort by popularity (if available) then price (lower).
     */
    public List<Property> recommendGeneric(RecommendRequest req) {
        List<Property> all = propertyRepository.findAll();
        Stream<Property> s = all.stream();

        if (req.getLocation() != null && !req.getLocation().isBlank()) {
            s = s.filter(p -> req.getLocation().equalsIgnoreCase(p.getLocation()));
        }
        if (req.getMinPrice() != null) {
            s = s.filter(p -> p.getPrice() >= req.getMinPrice());
        }
        if (req.getMaxPrice() != null) {
            s = s.filter(p -> p.getPrice() <= req.getMaxPrice());
        }

        Comparator<Property> comparator = Comparator
                .comparing((Property p) -> p.getViews() == null ? 0 : p.getViews()).reversed()
                .thenComparing(Property::getPrice);

        return s.sorted(comparator)
                .limit(Math.max(1, req.getK()))
                .collect(Collectors.toList());
    }

    // -----------------------------------------
    // Helpers
    // -----------------------------------------
    private double[] buildUserVectorFromHistoryOrProfile(String email, RecommendRequest req) {
        // Use user profile if available
        return userRepository.findByEmail(email)
                .map(u -> {
                    // if you store vector as double[] in user, return that
                    if (u.getVector() != null && u.getVector().length > 0) return u.getVector();
                    return approximateVectorFromUser(u, req);
                })
                .orElseGet(() -> approximateVectorFromRequest(req));
    }

    private double[] approximateVectorFromUser(com.project.society.model.User u, RecommendRequest req) {
        // Very small placeholder: encode preferred price band if present
        return approximateVectorFromRequest(req);
    }

    private double[] approximateVectorFromRequest(RecommendRequest req) {
        double price = req.getMinPrice() != null ? req.getMinPrice()
                : (req.getMaxPrice() != null ? req.getMaxPrice() : 0.0);
        // Simple 3-d vector [priceNormalized, locationFlag, bias]
        double pNorm = price / (price + 100000.0); // naive normalization
        double loc = (req.getLocation() == null || req.getLocation().isBlank()) ? 0.0 : 1.0;
        return new double[]{pNorm, loc, 1.0};
    }

    private double[] toVector(Property p) {
        double pNorm = p.getPrice() / (p.getPrice() + 100000.0);
        double hasImage = (p.getImage() != null && !p.getImage().isBlank()) ? 1.0 : 0.0;
        double popularity = (p.getViews() != null ? Math.min(1.0, p.getViews() / 1000.0) : 0.0);
        return new double[]{pNorm, hasImage, popularity};
    }

    private List<Property> applyFiltersAndPick(Map<Property, Double> scoreMap, RecommendRequest req, int k) {
        return scoreMap.entrySet().stream()
                .filter(e -> {
                    Property p = e.getKey();
                    if (req.getLocation() != null && !req.getLocation().isBlank() &&
                            !req.getLocation().equalsIgnoreCase(p.getLocation())) return false;
                    if (req.getMinPrice() != null && p.getPrice() < req.getMinPrice()) return false;
                    if (req.getMaxPrice() != null && p.getPrice() > req.getMaxPrice()) return false;
                    return true;
                })
                .sorted(Map.Entry.<Property, Double>comparingByValue().reversed())
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
