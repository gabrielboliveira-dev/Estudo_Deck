package com.example.estudodeck.application.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewForecastDto {
    private LocalDate date;
    private long cardCount;

    public ReviewForecastDto(LocalDate date, long cardCount) {
        this.date = date;
        this.cardCount = cardCount;
    }
}
