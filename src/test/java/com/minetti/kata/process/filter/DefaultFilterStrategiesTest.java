package com.minetti.kata.process.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultFilterStrategiesTest {

  @Test
  @DisplayName("Registers a strategy for every filter")
  void registersEveryFilter() {
    Set<SingleFilter> filters = DefaultFilterStrategies.create().stream()
        .map(FilterStrategy::filter)
        .collect(Collectors.toSet());

    assertEquals(EnumSet.allOf(SingleFilter.class), filters);
  }

  @Test
  @DisplayName("Registers each filter only once")
  void registersEachFilterOnce() {
    List<FilterStrategy> strategies = DefaultFilterStrategies.create();

    assertEquals(4, strategies.size());
    assertEquals(4, strategies.stream().map(FilterStrategy::filter).distinct().count());
  }
}
