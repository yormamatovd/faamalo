package org.example.faamalobot.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeDto {

    private Integer years;
    private Integer months;
    private Integer days;
    private Integer hours;
    private Integer minutes;
    private Integer seconds;
    private String zoneId;
}
