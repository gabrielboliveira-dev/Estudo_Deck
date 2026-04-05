package com.example.estudodeck.application.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
public class HeatmapDataDto {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Map<LocalDate, Long> dailyReviewCounts;
}
