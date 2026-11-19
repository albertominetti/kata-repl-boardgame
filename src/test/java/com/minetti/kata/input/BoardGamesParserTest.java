package com.minetti.kata.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minetti.kata.process.BoardGame;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardGamesParserTest {

  private static final String HEADER = "ID;Title;Tags;Rating;PlayTime;Price;Description\n";

  private final BoardGamesParser parser = new BoardGamesParser();

  @Test
  @DisplayName("Parses the bundled boardgames.csv file")
  void parsesBundledCsv() throws IOException {
    BoardGameList gameList = new LocalBoardGameList("/boardgames.csv");

    try (InputStream csvStream = gameList.getInputStream()) {
      assertNotNull(csvStream, "boardgames.csv should be on the classpath");

      List<BoardGame> games = parser.parseCsv(csvStream);

      assertEquals(151, games.size());

      BoardGame first = games.get(0);
      assertEquals(177590, first.id());
      assertEquals("13 Days: The Cuban Missile Crisis, 1962", first.title());
      assertEquals(List.of("Base Game", "Two Player", "Medium"), first.tags());
      assertEquals(4, first.rating());
      assertEquals(45, first.playTime());
      assertEquals(new BigDecimal("21.87"), first.price());
      assertEquals("BGG rank #1129. Community rating 7.2/10.", first.description());

      BoardGame last = games.get(150);
      assertEquals(342848, last.id());
      assertEquals("World of Warcraft: Wrath of the Lich King", last.title());
      assertEquals(List.of("Base Game", "Solo", "Multiplayer", "Medium"), last.tags());
    }
  }

  @Test
  @DisplayName("Keeps the user's favorite games")
  void keepsFavoriteGames() throws IOException {
    List<BoardGame> games;
    try (InputStream csvStream = new LocalBoardGameList("/boardgames.csv").getInputStream()) {
      games = parser.parse(csvStream);
    }

    BoardGame duel = games.stream()
        .filter(game -> game.title().equals("7 Wonders Duel"))
        .findFirst()
        .orElseThrow();

    assertEquals(173346, duel.id());
    assertEquals(5, duel.rating());
    assertEquals(30, duel.playTime());
    assertEquals(List.of("Base Game", "Two Player", "Medium"), duel.tags());

    BoardGame brass = games.stream()
        .filter(game -> game.title().equals("Brass: Birmingham"))
        .findFirst()
        .orElseThrow();

    assertEquals(4, brass.rating());
    assertEquals(120, brass.playTime());
  }

  @Test
  @DisplayName("Keeps expansions with their own tags")
  void keepsExpansions() throws IOException {
    List<BoardGame> games;
    try (InputStream csvStream = new LocalBoardGameList("/boardgames.csv").getInputStream()) {
      games = parser.parse(csvStream);
    }

    assertTrue(games.stream().anyMatch(game -> game.tags().contains("Expansion")));
  }

  @Test
  @DisplayName("Parses games from an InputStream")
  void parsesInputStream() throws IOException {
    BoardGameList gameList = new LocalBoardGameList("boardgames.csv");

    try (InputStream csvStream = gameList.getInputStream()) {
      assertNotNull(csvStream, "boardgames.csv should be on the classpath");

      List<BoardGame> games = parser.parse(csvStream);

      assertEquals(151, games.size());
      assertEquals("13 Days: The Cuban Missile Crisis, 1962", games.get(0).title());
    }
  }

  @Test
  @DisplayName("Reads games from a Reader")
  void parsesReader() {
    String csv = HEADER
        + "7;Interstellar;Base Game, Solo, Heavy;5;169;12.99;\"A journey.\"\n";

    List<BoardGame> games = parser.parse(new StringReader(csv));

    assertEquals(1, games.size());
    BoardGame game = games.get(0);
    assertEquals(7, game.id());
    assertEquals("Interstellar", game.title());
    assertEquals(List.of("Base Game", "Solo", "Heavy"), game.tags());
    assertEquals(5, game.rating());
    assertEquals(169, game.playTime());
    assertEquals(new BigDecimal("12.99"), game.price());
    assertEquals("A journey.", game.description());
  }

  @Test
  @DisplayName("Skips rows with too few columns")
  void skipsIncompleteRows() {
    String csv = HEADER
        + "1;Complete;Light;3;90;10.00;A description\n"
        + "2;Incomplete;Light\n";

    List<BoardGame> games = parser.parse(new StringReader(csv));

    assertEquals(1, games.size());
    assertEquals("Complete", games.get(0).title());
  }

  @Test
  @DisplayName("Treats a blank tag column as no tags")
  void handlesBlankTags() {
    String csv = HEADER + "1;No Tags;;3;90;10.00;A description\n";

    List<BoardGame> games = parser.parse(new StringReader(csv));

    assertEquals(1, games.size());
    assertTrue(games.get(0).tags().isEmpty());
  }

  @Test
  @DisplayName("Trims values around columns")
  void trimsValues() {
    String csv = HEADER + " 3 ; Padded ; Light, Solo ; 2 ; 101 ; 9.50 ; Padded description \n";

    BoardGame game = parser.parse(new StringReader(csv)).get(0);

    assertEquals(3, game.id());
    assertEquals("Padded", game.title());
    assertEquals(List.of("Light", "Solo"), game.tags());
    assertEquals(2, game.rating());
    assertEquals(101, game.playTime());
    assertEquals(new BigDecimal("9.50"), game.price());
    assertEquals("Padded description", game.description());
  }

  @Test
  @DisplayName("Rejects rows with invalid numbers")
  void rejectsInvalidNumbers() {
    String csv = HEADER + "1;Broken;Light;not-a-number;90;10.00;A description\n";

    assertThrows(CsvFormatException.class, () -> parser.parse(new StringReader(csv)));
  }

  @Test
  @DisplayName("Returns an empty list when only the header is present")
  void returnsEmptyListForHeaderOnly() {
    List<BoardGame> games = parser.parse(new StringReader(HEADER));

    assertTrue(games.isEmpty());
  }

  @Test
  @DisplayName("Parses the same games from an InputStream and a Reader")
  void parsesInputStreamAndReaderConsistently() {
    String csv = HEADER + "1;Same;Light;3;90;10.00;A description\n";

    List<BoardGame> fromReader = parser.parse(new StringReader(csv));
    List<BoardGame> fromStream = parser.parse(
        new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));

    assertEquals(fromReader, fromStream);
  }
}
