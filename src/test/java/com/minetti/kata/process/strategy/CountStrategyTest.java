package com.minetti.kata.process.strategy;

import static com.minetti.kata.support.BoardGameFixtures.GAMES;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CountStrategyTest {

  private final CountStrategy strategy = new CountStrategy();

  @Test
  @DisplayName("Declares the COUNT command")
  void declaresCommand() {
    assertEquals(SingleCommand.COUNT, strategy.command());
  }

  @Test
  @DisplayName("Counts all provided games")
  void countsGames() {
    assertEquals("2", strategy.execute(GAMES));
  }

  @Test
  @DisplayName("Counts zero for an empty list")
  void countsZeroForEmptyList() {
    assertEquals("0", strategy.execute(List.of()));
  }
}
