package com.minetti.kata.process.filter;

import java.util.List;

public final class DefaultFilterStrategies {

    private DefaultFilterStrategies() {
    }

    public static List<FilterStrategy> create() {
        return List.of(
                new TitleFilterStrategy(),
                new TagFilterStrategy(),
                new RatingFilterStrategy(),
                new PlayTimeFilterStrategy());
    }
}
