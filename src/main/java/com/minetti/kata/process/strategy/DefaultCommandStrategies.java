package com.minetti.kata.process.strategy;

import java.util.List;

public final class DefaultCommandStrategies {

    private DefaultCommandStrategies() {
    }

    public static List<CommandStrategy> create() {
        return List.of(
                new CountStrategy(),
                new AverageRatingStrategy(),
                new AveragePlayTimeStrategy());
    }
}
