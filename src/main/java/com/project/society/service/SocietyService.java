package com.project.society.service;

import com.project.society.model.*;
import com.project.society.repository.MemberRepository;
import com.project.society.repository.SocietyRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SocietyService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private MemberRepository memberRepository;

    // 1️⃣ Societies owned by a user (Owner)
    public List<Society> getSocietiesByOwner(String ownerId) {
        MatchOperation matchOwner = Aggregation.match(Criteria.where("ownerId").is(ownerId));
        Aggregation agg = Aggregation.newAggregation(matchOwner);
        return mongoTemplate.aggregate(agg, "societies", Society.class).getMappedResults();
    }

    // 2️⃣ Societies where a user is member, secretary, or watchman
    public List<Document> getSocietiesByUserRole(String userId) {
        MatchOperation matchMember = Aggregation.match(Criteria.where("userId").is(userId));
        LookupOperation lookupSociety = Aggregation.lookup("societies", "societyId", "_id", "society");
        Aggregation agg = Aggregation.newAggregation(matchMember, lookupSociety);
        return mongoTemplate.aggregate(agg, "members", Document.class).getMappedResults();
    }

    // 3️⃣ Count members per society
    public List<Document> countMembersPerSociety() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group("societyId").count().as("memberCount"),
                Aggregation.project("memberCount").and("_id").as("societyId")
        );
        return mongoTemplate.aggregate(agg, "members", Document.class).getMappedResults();
    }

    // 4️⃣ Complaints grouped by category and priority
    public List<Document> complaintsByCategoryPriority(String societyId) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("societyId").is(societyId)),
                Aggregation.group("category", "priority").count().as("total"),
                Aggregation.project("total")
                        .and("_id.category").as("category")
                        .and("_id.priority").as("priority")
        );
        return mongoTemplate.aggregate(agg, "complaints", Document.class).getMappedResults();
    }

    // 5️⃣ Upcoming events
    public List<EventCalendar> upcomingEvents(String societyId) {
        LocalDateTime now = LocalDateTime.now();
        MatchOperation match = Aggregation.match(
                Criteria.where("societyId").is(societyId)
                        .and("dateTime").gte(now)
        );
        SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.ASC, "dateTime"));
        Aggregation agg = Aggregation.newAggregation(match, sort);
        return mongoTemplate.aggregate(agg, "events", EventCalendar.class).getMappedResults();
    }

    // 6️⃣ Visitor entries by society and roles
    public List<VisitorEntry> getVisitorsBySociety(String societyId, List<String> userIds) {
        MatchOperation match = Aggregation.match(
                Criteria.where("societyId").is(societyId)
                        .and("userId").in(userIds)
        );
        SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.DESC, "createdAt"));
        Aggregation agg = Aggregation.newAggregation(match, sort);
        return mongoTemplate.aggregate(agg, "visitors", VisitorEntry.class).getMappedResults();
    }
    // 7️⃣ Maintenance Calculation API (Business Logic)
    public double calculateMaintenance(String userId) {
        // 1. Find the user's membership to get the society ID
        Member member = memberRepository.findByUserId(userId).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User is not a member of any society."));

        // 2. Fetch society details to get the base fee (Assumes you added the field to Society.java)
        Society society = societyRepository.findById(member.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found."));

        // 3. Calculation Logic (The business logic lives here!)
        double maintenanceAmount = society.getMonthlyMaintenanceFee();

        // Example: Apply a 10% discount if the member is a SECRETARY
        if ("SECRETARY".equals(member.getRole())) {
            maintenanceAmount *= 0.90;
        }

        return maintenanceAmount;
    }

    // 8️⃣ Facility Retrieval API (Business Logic)
    public List<String> getSocietyFacilities(String societyId) {
        // 1. Fetch society details (Assumes you added the facilities list to Society.java)
        Society society = societyRepository.findById(societyId)
                .orElseThrow(() -> new RuntimeException("Society not found."));

        // 2. Return the list, handling a null value gracefully
        return society.getFacilities() != null ? society.getFacilities() : List.of();
    }
}
