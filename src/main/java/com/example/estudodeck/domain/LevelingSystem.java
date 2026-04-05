package com.example.estudodeck.domain;

import org.springframework.stereotype.Service;

@Service
public class LevelingSystem {

    private static final int BASE_XP = 100;

    public int calculateLevel(long xp) {
        if (xp < 0) return 1;
        return (int) (Math.floor(25 + Math.sqrt(625 + 100 * xp)) / 50);
    }

    public long getXpForLevel(int level) {
        if (level <= 1) return 0;
        return (long) (25 * Math.pow(level, 2) - 25 * level);
    }

    public int calculateXpFromReview(int quality, CardMaturity maturity) {
        int base = switch (quality) {
            case 0, 1, 2 -> 1; // Low XP for failure
            case 3 -> 5;
            case 4 -> 10;
            case 5 -> 15;
            default -> 0;
        };
        // Bonus for mature cards
        return maturity == CardMaturity.MATURE ? base * 2 : base;
    }
}
