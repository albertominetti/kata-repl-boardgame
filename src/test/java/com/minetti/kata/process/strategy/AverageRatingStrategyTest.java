package com.minetti.kata.process.strategy;

import static com.minetti.kata.support.BoardGameFixtures.GAMES;
import static com.minetti.kata.support.BoardGameFixtures.SEA_SALT_AND_PAPER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AverageRatingStrategyTest {

  private final AverageRatingStrategy strategy = new AverageRatingStrategy();

  @Test
  @DisplayName("Declares the AVERAGE_RATING command")
  void declaresCommand() {
    assertEquals(SingleCommand.AVERAGE_RATING, strategy.command());
  }

  @Test
  @DisplayName("Averages the ratings of all games")
  void averagesRatings() {
    assertEquals("4.5", strategy.execute(GAMES));
  }

  @Test
  @DisplayName("Returns the rating of a single game")
  void returnsSingleRating() {
    assertEquals("5.0", strategy.execute(List.of(SEA_SALT_AND_PAPER)));
  }

  @Test
  @DisplayName("Returns zero for an empty list")
  void returnsZeroForEmptyList() {
    assertEquals("0.0", strategy.execute(List.of()));
  }
}
