package org.example.faamalobot.model;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
public class ExceptionDto {
    private String message;
    private LocalDateTime time;
    private String updateDto;
}
