package com.minetti.kata.input;

import static com.minetti.kata.support.BoardGameFixtures.BRASS_BIRMINGHAM;
import static com.minetti.kata.support.BoardGameFixtures.SEA_SALT_AND_PAPER;
import static com.minetti.kata.support.BoardGameFixtures.withTitle;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minetti.kata.process.BoardGame;
import com.minetti.kata.process.filter.TitleFilterStrategy;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FilterParserTest {

  private final FilterParser parser = new FilterParser();

  @Test
  @DisplayName("Parses a title filter ignoring case")
  void parsesTitleFilter() {
    Predicate<BoardGame> filter = parser.parse("FILTER_TITLE=SEA SALT").orElseThrow();

    assertTrue(filter.test(SEA_SALT_AND_PAPER));
    assertFalse(filter.test(BRASS_BIRMINGHAM));
  }

  @Test
  @DisplayName("Parses a tag filter ignoring case")
  void parsesTagFilter() {
    Predicate<BoardGame> filter = parser.parse("filter_tag=light").orElseThrow();

    assertTrue(filter.test(SEA_SALT_AND_PAPER));
    assertFalse(filter.test(BRASS_BIRMINGHAM));
  }

  @Test
  @DisplayName("Parses a rating filter")
  void parsesRatingFilter() {
    Predicate<BoardGame> filter = parser.parse("FILTER_RATING=4").orElseThrow();

    assertTrue(filter.test(BRASS_BIRMINGHAM));
    assertFalse(filter.test(SEA_SALT_AND_PAPER));
  }

  @Test
  @DisplayName("Parses a play time filter")
  void parsesPlayTimeFilter() {
    Predicate<BoardGame> filter = parser.parse("FILTER_PLAYTIME=120").orElseThrow();

    assertTrue(filter.test(BRASS_BIRMINGHAM));
    assertFalse(filter.test(SEA_SALT_AND_PAPER));
  }

  @Test
  @DisplayName("Trims the trigger and the value")
  void trimsTriggerAndValue() {
    Predicate<BoardGame> filter = parser.parse(" FILTER_RATING = 5 ").orElseThrow();

    assertTrue(filter.test(SEA_SALT_AND_PAPER));
  }

  @Test
  @DisplayName("Keeps the equals sign that appears in the value")
  void keepsEqualsSignInValue() {
    Predicate<BoardGame> filter = parser.parse("FILTER_TITLE=a=b").orElseThrow();

    assertTrue(filter.test(withTitle("a=b")));
    assertFalse(filter.test(withTitle("ab")));
  }

  @Test
  @DisplayName("Returns empty for tokens that are not filters")
  void returnsEmptyForNonFilterTokens() {
    assertTrue(parser.parse("COUNT").isEmpty());
    assertTrue(parser.parse("SOMETHING=1").isEmpty());
  }

  @Test
  @DisplayName("Rejects unknown FILTER_ tokens")
  void rejectsUnknownFilterTokens() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> parser.parse("FILTER_PRICE=10"));

    assertTrue(exception.getMessage().contains("FILTER_PRICE"));
  }

  @Test
  @DisplayName("Rejects supported filters without a value")
  void rejectsFiltersWithoutValue() {
    assertThrows(IllegalArgumentException.class, () -> parser.parse("FILTER_TITLE="));
    assertThrows(IllegalArgumentException.class, () -> parser.parse("FILTER_TAG=   "));
  }

  @Test
  @DisplayName("Uses the provided strategies")
  void usesProvidedStrategies() {
    FilterParser customParser = new FilterParser(List.of(new TitleFilterStrategy()));

    Predicate<BoardGame> filter = customParser.parse("FILTER_TITLE=salt").orElseThrow();

    assertTrue(filter.test(SEA_SALT_AND_PAPER));
    assertFalse(filter.test(BRASS_BIRMINGHAM));
  }
}
