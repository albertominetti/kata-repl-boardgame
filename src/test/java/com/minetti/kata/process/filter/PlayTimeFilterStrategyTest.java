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

class PlayTimeFilterStrategyTest {

  private final PlayTimeFilterStrategy strategy = new PlayTimeFilterStrategy();

  @Test
  @DisplayName("Declares the FILTER_PLAYTIME trigger")
  void declaresFilter() {
    assertEquals(SingleFilter.FILTER_PLAYTIME, strategy.filter());
  }

  @Test
  @DisplayName("Matches the exact play time")
  void matchesExactPlayTime() {
    Predicate<BoardGame> filter = strategy.create("120");

    assertTrue(filter.test(BRASS_BIRMINGHAM));
    assertFalse(filter.test(SEA_SALT_AND_PAPER));
  }

  @Test
  @DisplayName("Rejects a non-numeric play time")
  void rejectsNonNumericPlayTime() {
    assertThrows(IllegalArgumentException.class, () -> strategy.create("long"));
  }

  @Test
  @DisplayName("Rejects a blank play time")
  void rejectsBlankPlayTime() {
    assertThrows(IllegalArgumentException.class, () -> strategy.create(""));
  }
}
