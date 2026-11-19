package com.minetti.kata.process.filter;

import static com.minetti.kata.support.BoardGameFixtures.BRASS_BIRMINGHAM;
import static com.minetti.kata.support.BoardGameFixtures.SEA_SALT_AND_PAPER;
import static com.minetti.kata.support.BoardGameFixtures.withTitle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minetti.kata.process.BoardGame;
import java.util.function.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagFilterStrategyTest {

  private final TagFilterStrategy strategy = new TagFilterStrategy();

  @Test
  @DisplayName("Declares the FILTER_TAG trigger")
  void declaresFilter() {
    assertEquals(SingleFilter.FILTER_TAG, strategy.filter());
  }

  @Test
  @DisplayName("Matches any tag, ignoring case")
  void matchesAnyTagIgnoringCase() {
    Predicate<BoardGame> filter = strategy.create("light");

    assertTrue(filter.test(SEA_SALT_AND_PAPER));
    assertFalse(filter.test(BRASS_BIRMINGHAM));
  }

  @Test
  @DisplayName("Matches a tag regardless of its position")
  void matchesTagInAnyPosition() {
    assertTrue(strategy.create("Heavy").test(BRASS_BIRMINGHAM));
    assertTrue(strategy.create("Multiplayer").test(SEA_SALT_AND_PAPER));
  }

  @Test
  @DisplayName("Requires a full tag match")
  void requiresFullTagMatch() {
    assertFalse(strategy.create("Lig").test(SEA_SALT_AND_PAPER));
  }

  @Test
  @DisplayName("Does not match a game without tags")
  void doesNotMatchGameWithoutTags() {
    assertFalse(strategy.create("Light").test(withTitle("Untagged")));
  }
}
