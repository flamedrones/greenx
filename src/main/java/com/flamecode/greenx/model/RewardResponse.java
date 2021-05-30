package com.flamecode.greenx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardResponse {
    private String email;
    private String orderId;
    private Double tokenAmount;
    private String transactionId;
    private String etherscanUrl;
}
