package com.project.society.controller;

import com.project.society.dto.MaintenanceResponse;
import com.project.society.dto.RazorpayOrderResponse;

import com.project.society.service.MaintenanceService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService service;

    // =====================================
    // GET MAINTENANCE
    // =====================================

    @GetMapping

    @PreAuthorize(
            "hasAnyRole('MEMBER','OWNER','ADMIN','SECRETARY')"
    )

    public List<MaintenanceResponse> getMaintenance(

            @RequestParam String societyId,

            @RequestParam(required = false)
            String userId
    ){

        // SECRETARY / ADMIN

        if(userId == null){

            return service.getBySociety(
                    societyId
            );
        }

        // MEMBER

        return service.getByUser(

                societyId,

                userId
        );
    }

    // =====================================
    // CREATE ORDER
    // =====================================

    @PostMapping("/{id}/create-order")

    @PreAuthorize(
            "hasAnyRole('MEMBER','OWNER','ADMIN','SECRETARY')"
    )

    public RazorpayOrderResponse createOrder(

            @PathVariable String id,

            Authentication auth
    ){

        // DEBUG

        System.out.println(
                "AUTH: " + auth
        );

        System.out.println(
                "AUTHORITIES: " +
                        auth.getAuthorities()
        );

        return service.createOrder(id);
    }

    // =====================================
    // VERIFY PAYMENT
    // =====================================

    @PostMapping("/{id}/verify")

    @PreAuthorize(
            "hasAnyRole('MEMBER','OWNER','ADMIN','SECRETARY')"
    )

    public String verifyPayment(

            @PathVariable String id,

            @RequestBody Map<String,String> body
    ){

        String paymentId =
                body.get("paymentId");

        String signature =
                body.get("signature");

        service.verifyPayment(

                id,

                paymentId,

                signature
        );

        return "Payment verified";
    }
}