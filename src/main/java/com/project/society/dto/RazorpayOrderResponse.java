package com.project.society.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayOrderResponse {

    private String orderId;

    private String key;

    private Integer amount;

    private String currency;
}