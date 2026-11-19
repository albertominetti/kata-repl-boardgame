package com.minetti.kata.input;

import com.minetti.kata.process.strategy.SingleCommand;
import com.minetti.kata.process.BoardGame;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public record CommandRequest(List<SingleCommand> commands, List<Predicate<BoardGame>> filters) {

    public CommandRequest {
        commands = List.copyOf(Objects.requireNonNull(commands, "commands"));
        filters = List.copyOf(Objects.requireNonNull(filters, "filters"));
    }
}
