package com.minetti.kata.process.filter;

import com.minetti.kata.process.BoardGame;
import java.util.function.Predicate;

public interface FilterStrategy {

    SingleFilter filter();

    Predicate<BoardGame> create(String value);
}
