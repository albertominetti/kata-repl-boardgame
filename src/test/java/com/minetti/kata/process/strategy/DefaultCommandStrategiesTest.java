package com.minetti.kata.process.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultCommandStrategiesTest {

  @Test
  @DisplayName("Registers a strategy for every executable command")
  void registersEveryCommand() {
    Set<SingleCommand> commands = DefaultCommandStrategies.create().stream()
        .map(CommandStrategy::command)
        .collect(Collectors.toSet());

    assertEquals(
        EnumSet.of(SingleCommand.COUNT, SingleCommand.AVERAGE_RATING, SingleCommand.AVERAGE_PLAYTIME),
        commands);
  }

  @Test
  @DisplayName("Does not register the EXIT command")
  void doesNotRegisterExit() {
    List<CommandStrategy> strategies = DefaultCommandStrategies.create();

    assertEquals(3, strategies.size());
    assertEquals(3, strategies.stream().map(CommandStrategy::command).distinct().count());
  }
}
