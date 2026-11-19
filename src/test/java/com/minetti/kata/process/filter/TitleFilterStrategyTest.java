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

class TitleFilterStrategyTest {

  private final TitleFilterStrategy strategy = new TitleFilterStrategy();

  @Test
  @DisplayName("Declares the FILTER_TITLE trigger")
  void declaresFilter() {
    assertEquals(SingleFilter.FILTER_TITLE, strategy.filter());
  }

  @Test
  @DisplayName("Matches titles containing the value, ignoring case")
  void matchesTitleIgnoringCase() {
    Predicate<BoardGame> filter = strategy.create("sea salt");

    assertTrue(filter.test(SEA_SALT_AND_PAPER));
    assertFalse(filter.test(BRASS_BIRMINGHAM));
  }

  @Test
  @DisplayName("Matches any part of the title")
  void matchesAnyPartOfTitle() {
    assertTrue(strategy.create("Salt").test(SEA_SALT_AND_PAPER));
  }

  @Test
  @DisplayName("Does not match a different title")
  void doesNotMatchDifferentTitle() {
    assertFalse(strategy.create("Catan").test(SEA_SALT_AND_PAPER));
  }

  @Test
  @DisplayName("Matches every title for an empty value")
  void matchesEveryTitleForEmptyValue() {
    Predicate<BoardGame> filter = strategy.create("");

    assertTrue(filter.test(withTitle("Anything")));
    assertTrue(filter.test(withTitle("")));
  }
}
