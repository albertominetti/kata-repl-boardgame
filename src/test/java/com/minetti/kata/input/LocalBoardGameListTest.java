package com.minetti.kata.input;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LocalBoardGameListTest {

  @Test
  @DisplayName("Loads a classpath resource with a leading slash")
  void loadsResourceWithLeadingSlash() throws IOException {
    try (InputStream stream = new LocalBoardGameList("/boardgames.csv").getInputStream()) {
      assertNotNull(stream);
      assertTrue(new String(stream.readAllBytes(), StandardCharsets.UTF_8).startsWith("ID;Title"));
    }
  }

  @Test
  @DisplayName("Loads a classpath resource without a leading slash")
  void loadsResourceWithoutLeadingSlash() throws IOException {
    try (InputStream stream = new LocalBoardGameList("boardgames.csv").getInputStream()) {
      assertNotNull(stream);
    }
  }

  @Test
  @DisplayName("Fails when the resource does not exist")
  void failsForMissingResource() {
    LocalBoardGameList gameList = new LocalBoardGameList("/missing.csv");

    CsvFormatException exception =
        assertThrows(CsvFormatException.class, gameList::getInputStream);

    assertTrue(exception.getMessage().contains("/missing.csv"));
  }

  @Test
  @DisplayName("Fails for a missing resource without a leading slash")
  void failsForMissingResourceWithoutLeadingSlash() {
    LocalBoardGameList gameList = new LocalBoardGameList("missing.csv");

    CsvFormatException exception =
        assertThrows(CsvFormatException.class, gameList::getInputStream);

    assertTrue(exception.getMessage().contains("/missing.csv"));
  }
}
