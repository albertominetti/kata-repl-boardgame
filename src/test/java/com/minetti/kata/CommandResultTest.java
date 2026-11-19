package com.minetti.kata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommandResultTest {

  @Test
  @DisplayName("Keeps the results in order")
  void keepsResultOrder() {
    CommandResult result = new CommandResult(List.of("1", "4.5"));

    assertEquals(List.of("1", "4.5"), result.results());
  }

  @Test
  @DisplayName("Copies results defensively")
  void copiesResultsDefensively() {
    List<String> results = new ArrayList<>(List.of("2"));

    CommandResult result = new CommandResult(results);
    results.add("3");

    assertEquals(List.of("2"), result.results());
  }

  @Test
  @DisplayName("Exposes immutable results")
  void exposesImmutableResults() {
    CommandResult result = new CommandResult(List.of("2"));

    assertThrows(UnsupportedOperationException.class, () -> result.results().add("3"));
  }

  @Test
  @DisplayName("Rejects null results")
  void rejectsNullResults() {
    assertThrows(NullPointerException.class, () -> new CommandResult(null));
  }

  @Test
  @DisplayName("Accepts empty results")
  void acceptsEmptyResults() {
    assertTrue(new CommandResult(List.of()).results().isEmpty());
  }
}
