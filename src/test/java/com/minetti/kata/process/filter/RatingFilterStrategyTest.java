package com.minetti.kata.process.filter;

import static com.minetti.kata.support.BoardGameFixtures.BRASS_BIRMINGHAM;
import static com.minetti.kata.support.BoardGameFixtures.SEA_SALT_AND_PAPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minetti.kata.process.BoardGame;
import java.util.function.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RatingFilterStrategyTest {

  private final RatingFilterStrategy strategy = new RatingFilterStrategy();

  @Test
  @DisplayName("Declares the FILTER_RATING trigger")
  void declaresFilter() {
    assertEquals(SingleFilter.FILTER_RATING, strategy.filter());
  }

  @Test
  @DisplayName("Matches the exact rating")
  void matchesExactRating() {
    Predicate<BoardGame> filter = strategy.create("5");

    assertTrue(filter.test(SEA_SALT_AND_PAPER));
    assertFalse(filter.test(BRASS_BIRMINGHAM));
  }

  @Test
  @DisplayName("Rejects a non-numeric rating")
  void rejectsNonNumericRating() {
    assertThrows(IllegalArgumentException.class, () -> strategy.create("five"));
  }

  @Test
  @DisplayName("Rejects a blank rating")
  void rejectsBlankRating() {
    assertThrows(IllegalArgumentException.class, () -> strategy.create(" "));
  }
}
