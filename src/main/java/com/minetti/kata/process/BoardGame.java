package com.minetti.kata.process;

import java.math.BigDecimal;
import java.util.List;

public record BoardGame(
        int id,
        String title,
        List<String> tags,
        int rating,
        int playTime,
        BigDecimal price,
        String description
) {
}
