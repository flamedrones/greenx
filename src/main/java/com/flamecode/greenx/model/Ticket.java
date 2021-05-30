package com.flamecode.greenx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String startDestination;
    private String endDestination;
    private String platform;
    private Time timeLeave;
    private Time timeArrive;
    private Double distance;
    private Double price;
    private Double rewardToken;
}
