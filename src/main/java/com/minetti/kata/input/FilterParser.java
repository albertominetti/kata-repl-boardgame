package com.minetti.kata.input;

import com.minetti.kata.process.BoardGame;
import com.minetti.kata.process.filter.DefaultFilterStrategies;
import com.minetti.kata.process.filter.FilterStrategy;
import com.minetti.kata.process.filter.SingleFilter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterParser {

    private static final String FILTER_PREFIX = "FILTER_";

    private final Map<SingleFilter, FilterStrategy> strategies;

    public FilterParser() {
        this(DefaultFilterStrategies.create());
    }

    public FilterParser(List<FilterStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(FilterStrategy::filter, Function.identity()));
    }

    public Optional<Predicate<BoardGame>> parse(String token) {
        int separator = token.indexOf('=');
        if (separator < 0) {
            rejectInvalidFilter(token);
            return Optional.empty();
        }

        String trigger = token.substring(0, separator).trim();
        String value = token.substring(separator + 1).trim();
        SingleFilter filter = toFilter(trigger);
        if (filter == null) {
            rejectInvalidFilter(token);
            return Optional.empty();
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Missing value for " + filter);
        }
        return Optional.of(strategies.get(filter).create(value));
    }

    private static SingleFilter toFilter(String trigger) {
        for (SingleFilter filter : SingleFilter.values()) {
            if (filter.name().equalsIgnoreCase(trigger)) {
                return filter;
            }
        }
        return null;
    }

    private static void rejectInvalidFilter(String token) {
        if (token.regionMatches(true, 0, FILTER_PREFIX, 0, FILTER_PREFIX.length())) {
            throw new IllegalArgumentException("Invalid filter: " + token);
        }
    }
}
