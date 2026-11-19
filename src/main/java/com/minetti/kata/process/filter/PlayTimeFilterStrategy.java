package com.minetti.kata.process.filter;

import com.minetti.kata.process.BoardGame;
import java.util.function.Predicate;

public class PlayTimeFilterStrategy implements FilterStrategy {

    @Override
    public SingleFilter filter() {
        return SingleFilter.FILTER_PLAYTIME;
    }

    @Override
    public Predicate<BoardGame> create(String value) {
        int expected = parseInt(value);
        return game -> game.playTime() == expected;
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value for FILTER_PLAYTIME: " + value, e);
        }
    }
}
