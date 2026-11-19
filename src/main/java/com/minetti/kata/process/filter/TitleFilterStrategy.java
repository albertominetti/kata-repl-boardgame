package com.minetti.kata.process.filter;

import com.minetti.kata.process.BoardGame;
import java.util.Locale;
import java.util.function.Predicate;

public class TitleFilterStrategy implements FilterStrategy {

    @Override
    public SingleFilter filter() {
        return SingleFilter.FILTER_TITLE;
    }

    @Override
    public Predicate<BoardGame> create(String value) {
        String expected = value.toLowerCase(Locale.ROOT);
        return game -> game.title().toLowerCase(Locale.ROOT).contains(expected);
    }
}
