package com.minetti.kata.input;

import static com.minetti.kata.support.BoardGameFixtures.SEA_SALT_AND_PAPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minetti.kata.process.strategy.SingleCommand;
import com.minetti.kata.process.BoardGame;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommandRequestTest {

  @Test
  @DisplayName("Copies commands and filters defensively")
  void copiesInputsDefensively() {
    List<SingleCommand> commands = new ArrayList<>(List.of(SingleCommand.COUNT));
    List<Predicate<BoardGame>> filters = new ArrayList<>();
    filters.add(game -> true);

    CommandRequest request = new CommandRequest(commands, filters);

    commands.add(SingleCommand.EXIT);
    filters.clear();

    assertEquals(List.of(SingleCommand.COUNT), request.commands());
    assertEquals(1, request.filters().size());
  }

  @Test
  @DisplayName("Exposes immutable collections")
  void exposesImmutableCollections() {
    CommandRequest request = new CommandRequest(List.of(SingleCommand.COUNT), List.of());

    assertThrows(UnsupportedOperationException.class, () -> request.commands().add(SingleCommand.EXIT));
    assertThrows(UnsupportedOperationException.class, () -> request.filters().add(game -> true));
  }

  @Test
  @DisplayName("Rejects null commands and filters")
  void rejectsNulls() {
    assertThrows(NullPointerException.class, () -> new CommandRequest(null, List.of()));
    assertThrows(NullPointerException.class, () -> new CommandRequest(List.of(), null));
  }

  @Test
  @DisplayName("Accepts empty commands and filters")
  void acceptsEmptyLists() {
    CommandRequest request = new CommandRequest(List.of(), List.of());

    assertTrue(request.commands().isEmpty());
    assertTrue(request.filters().isEmpty());
  }

  @Test
  @DisplayName("Applies the stored filters to games")
  void appliesStoredFilters() {
    Predicate<BoardGame> titleFilter = game -> game.title().contains("Salt");
    CommandRequest request = new CommandRequest(List.of(SingleCommand.COUNT), List.of(titleFilter));

    assertTrue(request.filters().get(0).test(SEA_SALT_AND_PAPER));
  }
}
