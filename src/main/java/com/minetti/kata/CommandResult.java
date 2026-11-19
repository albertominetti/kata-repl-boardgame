package com.minetti.kata;

import java.util.List;
import java.util.Objects;

public record CommandResult(List<String> results) {

    public CommandResult {
        results = List.copyOf(Objects.requireNonNull(results, "results"));
    }
}
