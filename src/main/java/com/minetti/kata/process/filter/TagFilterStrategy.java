package com.minetti.kata.process.filter;

import com.minetti.kata.process.BoardGame;
import java.util.function.Predicate;

public class TagFilterStrategy implements FilterStrategy {

    @Override
    public SingleFilter filter() {
        return SingleFilter.FILTER_TAG;
    }

    @Override
    public Predicate<BoardGame> create(String value) {
        return game -> game.tags().stream()
                .anyMatch(tag -> tag.equalsIgnoreCase(value));
    }
}
