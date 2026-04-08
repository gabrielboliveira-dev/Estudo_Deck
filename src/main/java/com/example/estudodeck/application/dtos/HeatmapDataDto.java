package com.example.estudodeck.application.dtos;

import java.time.LocalDate;
import java.util.Map;

public record HeatmapDataDto(
        LocalDate startDate,
        LocalDate endDate,
        Map<LocalDate, Long> dailyCounts
) {}