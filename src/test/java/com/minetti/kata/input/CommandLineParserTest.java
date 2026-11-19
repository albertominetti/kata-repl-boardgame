package com.minetti.kata.input;

import static com.minetti.kata.support.BoardGameFixtures.BRASS_BIRMINGHAM;
import static com.minetti.kata.support.BoardGameFixtures.GAMES;
import static com.minetti.kata.support.BoardGameFixtures.SEA_SALT_AND_PAPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minetti.kata.process.strategy.SingleCommand;
import com.minetti.kata.process.BoardGame;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommandLineParserTest {

  private final CommandLineParser parser = new CommandLineParser();

  @Test
  @DisplayName("Parses a single command without filters")
  void parsesCommandWithoutFilters() {
    CommandRequest request = parser.parse("count");

    assertEquals(List.of(SingleCommand.COUNT), request.commands());
    assertTrue(request.filters().isEmpty());
  }

  @Test
  @DisplayName("Parses multiple commands and filters in any order")
  void parsesMultipleCommandsAndFilters() {
    CommandRequest request = parser.parse(
        "FILTER_PLAYTIME=120 COUNT AVERAGE_RATING FILTER_TAG=Heavy");

    assertEquals(List.of(SingleCommand.COUNT, SingleCommand.AVERAGE_RATING), request.commands());
    assertEquals(2, request.filters().size());
    assertEquals(List.of(BRASS_BIRMINGHAM), filter(request));
  }

  @Test
  @DisplayName("Parses a filter before the command")
  void parsesFilterBeforeCommand() {
    CommandRequest request = parser.parse("FILTER_TAG=Light AVERAGE_RATING");

    assertEquals(List.of(SingleCommand.AVERAGE_RATING), request.commands());
    assertEquals(List.of(SEA_SALT_AND_PAPER), filter(request));
  }

  @Test
  @DisplayName("Keeps the order in which commands are declared")
  void keepsCommandOrder() {
    CommandRequest request = parser.parse("AVERAGE_PLAYTIME COUNT AVERAGE_RATING");

    assertEquals(
        List.of(SingleCommand.AVERAGE_PLAYTIME, SingleCommand.COUNT, SingleCommand.AVERAGE_RATING),
        request.commands());
  }

  @Test
  @DisplayName("Filters titles using contains")
  void filtersTitleUsingContains() {
    CommandRequest request = parser.parse("FILTER_TITLE=salt COUNT");

    assertEquals(List.of(SEA_SALT_AND_PAPER), filter(request));
  }

  @Test
  @DisplayName("Filters ratings using exact match")
  void filtersRatingExactMatch() {
    CommandRequest request = parser.parse("FILTER_RATING=5 COUNT");

    assertEquals(List.of(SEA_SALT_AND_PAPER), filter(request));
  }

  @Test
  @DisplayName("Parses the EXIT command")
  void parsesExit() {
    assertEquals(List.of(SingleCommand.EXIT), parser.parse("EXIT").commands());
  }

  @Test
  @DisplayName("Accepts commands and filters in any case")
  void acceptsCommandsAndFiltersInAnyCase() {
    CommandRequest request = parser.parse("filter_tag=light Count");

    assertEquals(List.of(SingleCommand.COUNT), request.commands());
    assertEquals(List.of(SEA_SALT_AND_PAPER), filter(request));
  }

  @Test
  @DisplayName("Returns an empty request for blank or null input")
  void returnsEmptyRequestForBlankInput() {
    assertTrue(parser.parse("").commands().isEmpty());
    assertTrue(parser.parse("   ").commands().isEmpty());
    assertTrue(parser.parse(null).commands().isEmpty());
    assertTrue(parser.parse(null).filters().isEmpty());
  }

  @Test
  @DisplayName("Keeps quoted values as a single token and strips the quotes")
  void keepsQuotedValuesAsSingleToken() {
    CommandRequest request = parser.parse("FILTER_TITLE=\"Sea Salt & Paper\" COUNT");

    assertEquals(List.of(SEA_SALT_AND_PAPER), filter(request));
  }

  @Test
  @DisplayName("Supports single quoted values")
  void supportsSingleQuotedValues() {
    CommandRequest request = parser.parse("FILTER_TITLE='Brass' COUNT");

    assertEquals(List.of(BRASS_BIRMINGHAM), filter(request));
  }

  @Test
  @DisplayName("Rejects unterminated quotes")
  void rejectsUnterminatedQuotes() {
    assertThrows(IllegalArgumentException.class, () -> parser.parse("FILTER_TITLE=\"Brass COUNT"));
  }

  @Test
  @DisplayName("Requires at least one command")
  void requiresAtLeastOneCommand() {
    assertThrows(IllegalArgumentException.class, () -> parser.parse("FILTER_TAG=Light"));
  }

  @Test
  @DisplayName("Rejects unknown commands")
  void rejectsUnknownCommands() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> parser.parse("UNKNOWN"));

    assertTrue(exception.getMessage().contains("UNKNOWN"));
  }

  @Test
  @DisplayName("Rejects unsupported filters and operators")
  void rejectsUnsupportedFiltersAndOperators() {
    assertThrows(IllegalArgumentException.class, () -> parser.parse("FILTER_PRICE=10 COUNT"));
    assertThrows(IllegalArgumentException.class, () -> parser.parse("FILTER_RATING>=4 COUNT"));
  }

  @Test
  @DisplayName("Rejects filters without a value")
  void rejectsFiltersWithoutValue() {
    assertThrows(IllegalArgumentException.class, () -> parser.parse("FILTER_TITLE= COUNT"));
  }

  @Test
  @DisplayName("Rejects invalid numeric filter values")
  void rejectsInvalidNumericFilterValues() {
    assertThrows(IllegalArgumentException.class, () -> parser.parse("FILTER_RATING=abc COUNT"));
    assertThrows(IllegalArgumentException.class, () -> parser.parse("FILTER_PLAYTIME=abc COUNT"));
  }

  private static List<BoardGame> filter(CommandRequest request) {
    return GAMES.stream()
        .filter(game -> request.filters().stream().allMatch(filter -> filter.test(game)))
        .toList();
  }
}
