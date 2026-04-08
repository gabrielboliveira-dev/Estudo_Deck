package com.example.estudodeck.application.dtos;

import java.time.LocalDate;

public record ReviewForecastDto(
        LocalDate date,
        long count
) {}