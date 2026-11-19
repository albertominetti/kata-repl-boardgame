package com.minetti.kata.process.strategy;

import static com.minetti.kata.support.BoardGameFixtures.BRASS_BIRMINGHAM;
import static com.minetti.kata.support.BoardGameFixtures.GAMES;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AveragePlayTimeStrategyTest {

  private final AveragePlayTimeStrategy strategy = new AveragePlayTimeStrategy();

  @Test
  @DisplayName("Declares the AVERAGE_PLAYTIME command")
  void declaresCommand() {
    assertEquals(SingleCommand.AVERAGE_PLAYTIME, strategy.command());
  }

  @Test
  @DisplayName("Averages the play times of all games")
  void averagesPlayTimes() {
    assertEquals("82.5", strategy.execute(GAMES));
  }

  @Test
  @DisplayName("Returns the play time of a single game")
  void returnsSinglePlayTime() {
    assertEquals("120.0", strategy.execute(List.of(BRASS_BIRMINGHAM)));
  }

  @Test
  @DisplayName("Returns zero for an empty list")
  void returnsZeroForEmptyList() {
    assertEquals("0.0", strategy.execute(List.of()));
  }
}
