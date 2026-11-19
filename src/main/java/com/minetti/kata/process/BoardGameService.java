package com.minetti.kata.process;

import com.minetti.kata.input.CommandRequest;
import com.minetti.kata.CommandResult;
import com.minetti.kata.process.strategy.SingleCommand;
import com.minetti.kata.process.strategy.CommandStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BoardGameService {

    private final Map<SingleCommand, CommandStrategy> strategies;

    public BoardGameService(List<CommandStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(CommandStrategy::command, Function.identity()));
    }

    public CommandResult execute(CommandRequest request, List<BoardGame> games) {
        List<BoardGame> filteredGames = games.stream()
                .filter(game -> request.filters().stream()
                        .allMatch(filter -> filter.test(game))
                ).toList();

        List<String> results = new ArrayList<>();
        for (SingleCommand command : request.commands()) {
            if (command == SingleCommand.EXIT) {
                break;
            }
            results.add(strategies.get(command).execute(filteredGames));
        }
        return new CommandResult(results);
    }
}
