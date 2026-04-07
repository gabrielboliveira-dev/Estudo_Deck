package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.dtos.HeatmapDataDto;
import com.example.estudodeck.application.dtos.RetentionStatsDto;
import com.example.estudodeck.application.dtos.StreakDto;
import com.example.estudodeck.application.usecases.GetCurrentStreakUseCase;
import com.example.estudodeck.application.usecases.GetHeatmapDataUseCase;
import com.example.estudodeck.application.usecases.GetRetentionStatsUseCase;
import com.example.estudodeck.domain.LevelingSystem;
import com.example.estudodeck.infrastructure.persistence.entities.UserJpaEntity;
import com.example.estudodeck.infrastructure.security.UserContext;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final GetHeatmapDataUseCase getHeatmapDataUseCase;
    private final GetRetentionStatsUseCase getRetentionStatsUseCase;
    private final GetCurrentStreakUseCase getCurrentStreakUseCase;
    private final UserContext userContext;
    private final LevelingSystem levelingSystem;

    public DashboardController(GetHeatmapDataUseCase getHeatmapDataUseCase, GetRetentionStatsUseCase getRetentionStatsUseCase, GetCurrentStreakUseCase getCurrentStreakUseCase, UserContext userContext, LevelingSystem levelingSystem) {
        this.getHeatmapDataUseCase = getHeatmapDataUseCase;
        this.getRetentionStatsUseCase = getRetentionStatsUseCase;
        this.getCurrentStreakUseCase = getCurrentStreakUseCase;
        this.userContext = userContext;
        this.levelingSystem = levelingSystem;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        UserJpaEntity user = userContext.getAuthenticatedUser();

        model.addAttribute("user", user);
        model.addAttribute("xpForNextLevel", levelingSystem.getXpForLevel(user.getLevel() + 1));

        StreakDto streak = getCurrentStreakUseCase.execute();
        model.addAttribute("streak", streak);

        HeatmapDataDto heatmapData = getHeatmapDataUseCase.execute(new GetHeatmapDataUseCase.Input(365));
        List<HeatmapDay> heatmapDays = buildHeatmapDays(heatmapData);
        model.addAttribute("heatmapDays", heatmapDays);

        RetentionStatsDto stats = getRetentionStatsUseCase.execute();
        model.addAttribute("stats", stats);

        return "dashboard";
    }

    private List<HeatmapDay> buildHeatmapDays(HeatmapDataDto data) {
        List<HeatmapDay> days = new ArrayList<>();

        LocalDate currentDate = data.startDate();
        Map<LocalDate, Long> counts = data.dailyCounts();

        while (!currentDate.isAfter(data.endDate())) {
            long count = counts.getOrDefault(currentDate, 0L);
            int level = calculateLevel(count);
            days.add(new HeatmapDay(currentDate.toString(), count, level));
            currentDate = currentDate.plusDays(1);
        }
        return days;
    }

    private int calculateLevel(long count) {
        if (count == 0) return 0;
        if (count <= 5) return 1;
        if (count <= 15) return 2;
        if (count <= 30) return 3;
        return 4;
    }

    @Data
    public static class HeatmapDay {
        private final String date;
        private final long count;
        private final int level;
    }
}