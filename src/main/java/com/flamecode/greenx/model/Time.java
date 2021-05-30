package com.flamecode.greenx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Time {
    Integer month;
    Integer day;
    Integer hour;
    Integer minute;
}
