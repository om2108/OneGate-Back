package com.project.society.service;

import com.project.society.dto.MaintenanceResponse;
import com.project.society.dto.RazorpayOrderResponse;

import com.project.society.model.Maintenance;

import com.project.society.repository.MaintenanceRepository;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import lombok.RequiredArgsConstructor;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository repo;

    // =====================================
    // RAZORPAY
    // =====================================

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    // =====================================
    // GET MAINTENANCE
    // =====================================

    public List<MaintenanceResponse> getByUser(

            String societyId,

            String userId
    ) {

        List<Maintenance> list =

                repo.findBySocietyIdAndUserId(

                        societyId,

                        userId
                );

        return list.stream()

                .map(this::mapToResponse)

                .collect(Collectors.toList());
    }

    // =====================================
    // CREATE ORDER
    // =====================================

    public RazorpayOrderResponse createOrder(
            String maintenanceId
    ) {

        try {

            Maintenance maintenance =

                    repo.findById(maintenanceId)

                            .orElseThrow(() ->

                                    new RuntimeException(
                                            "Maintenance not found"
                                    )
                            );

            RazorpayClient client =

                    new RazorpayClient(

                            razorpayKey,

                            razorpaySecret
                    );

            JSONObject options =
                    new JSONObject();

            // Razorpay amount in paise

            options.put(

                    "amount",

                    (int) (maintenance.getAmount() * 100)
            );

            options.put(
                    "currency",
                    "INR"
            );

            options.put(
                    "receipt",
                    maintenance.getId()
            );

            Order order =
                    client.orders.create(options);

            return new RazorpayOrderResponse(

                    order.get("id"),

                    razorpayKey,

                    order.get("amount"),

                    order.get("currency")
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    e.getMessage()
            );
        }
    }

    // =====================================
    // VERIFY PAYMENT
    // =====================================

    public void verifyPayment(

            String maintenanceId,

            String paymentId,

            String signature
    ) {

        Maintenance maintenance =

                repo.findById(maintenanceId)

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Maintenance not found"
                                )
                        );

        // OPTIONAL:
        // Add Razorpay signature verification

        maintenance.setPaymentStatus(
                "PAID"
        );

        repo.save(maintenance);
    }

    // =====================================
    // DTO MAP
    // =====================================

    private MaintenanceResponse mapToResponse(
            Maintenance m
    ) {

        return new MaintenanceResponse(

                m.getId(),

                m.getSocietyId(),

                m.getUserId(),

                m.getResidentName(),

                m.getFlatNumber(),

                m.getAmount(),

                m.getMonth(),

                m.getYear(),

                m.getDueDate(),

                m.getPaymentStatus()
        );
    }
    // =====================================
// GET BY SOCIETY
// =====================================

    public List<MaintenanceResponse>
    getBySociety(
            String societyId
    ) {

        List<Maintenance> list =

                repo.findBySocietyId(
                        societyId
                );

        return list.stream()

                .map(this::mapToResponse)

                .collect(Collectors.toList());
    }
}