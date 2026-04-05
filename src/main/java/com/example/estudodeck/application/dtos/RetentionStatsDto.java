package com.example.estudodeck.application.dtos;

import lombok.Data;

@Data
public class RetentionStatsDto {
    private final double youngCardRetention;
    private final double matureCardRetention;
}
