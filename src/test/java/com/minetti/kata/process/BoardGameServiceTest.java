package com.minetti.kata.process;

import static com.minetti.kata.support.BoardGameFixtures.GAMES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minetti.kata.CommandResult;
import com.minetti.kata.input.CommandLineParser;
import com.minetti.kata.process.strategy.CountStrategy;
import com.minetti.kata.process.strategy.DefaultCommandStrategies;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardGameServiceTest {

  private final CommandLineParser parser = new CommandLineParser();

  private final BoardGameService service = new BoardGameService(DefaultCommandStrategies.create());

  @Test
  @DisplayName("Counts filtered games")
  void countsFilteredGames() {
    CommandResult result = service.execute(parser.parse("FILTER_TAG=Light COUNT"), GAMES);

    assertEquals(List.of("1"), result.results());
  }

  @Test
  @DisplayName("Executes multiple commands in order using the same filters")
  void executesMultipleCommandsInOrder() {
    CommandResult result = service.execute(
        parser.parse("FILTER_PLAYTIME=120 COUNT AVERAGE_RATING FILTER_TAG=Heavy"), GAMES);

    assertEquals(List.of("1", "4.0"), result.results());
  }

  @Test
  @DisplayName("Computes averages over all games")
  void computesAverages() {
    CommandResult result = service.execute(parser.parse("AVERAGE_RATING AVERAGE_PLAYTIME"), GAMES);

    assertEquals(List.of("4.5", "82.5"), result.results());
  }

  @Test
  @DisplayName("Stops at EXIT and skips following commands")
  void stopsAtExit() {
    CommandResult result = service.execute(parser.parse("COUNT EXIT COUNT"), GAMES);

    assertEquals(List.of("2"), result.results());
  }

  @Test
  @DisplayName("Returns no results when EXIT is the only command")
  void returnsNoResultsForExitOnly() {
    CommandResult result = service.execute(parser.parse("EXIT"), GAMES);

    assertTrue(result.results().isEmpty());
  }

  @Test
  @DisplayName("Counts zero when no game matches the filters")
  void countsZeroWhenNothingMatches() {
    CommandResult result = service.execute(parser.parse("FILTER_TITLE=unknown COUNT"), GAMES);

    assertEquals(List.of("0"), result.results());
  }

  @Test
  @DisplayName("Computes zero averages when no game matches the filters")
  void computesZeroAveragesWhenNothingMatches() {
    CommandResult result = service.execute(
        parser.parse("FILTER_TITLE=unknown AVERAGE_RATING AVERAGE_PLAYTIME"), GAMES);

    assertEquals(List.of("0.0", "0.0"), result.results());
  }

  @Test
  @DisplayName("Combines several filters with AND semantics")
  void combinesFiltersWithAndSemantics() {
    assertEquals(
        List.of("1"),
        service.execute(parser.parse("FILTER_PLAYTIME=120 FILTER_RATING=4 COUNT"), GAMES).results());

    assertEquals(
        List.of("0"),
        service.execute(parser.parse("FILTER_PLAYTIME=120 FILTER_RATING=5 COUNT"), GAMES).results());
  }

  @Test
  @DisplayName("Applies filters to games regardless of filter position")
  void appliesFiltersRegardlessOfPosition() {
    CommandResult result = service.execute(
        parser.parse("COUNT FILTER_TAG=Light AVERAGE_RATING"), GAMES);

    assertEquals(List.of("1", "5.0"), result.results());
  }

  @Test
  @DisplayName("Uses the provided strategies")
  void usesProvidedStrategies() {
    BoardGameService customService = new BoardGameService(List.of(new CountStrategy()));

    assertEquals(List.of("2"), customService.execute(parser.parse("COUNT"), GAMES).results());
  }
}
