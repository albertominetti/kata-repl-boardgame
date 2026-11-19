package com.minetti.kata.input;

import com.minetti.kata.process.BoardGame;
import com.minetti.kata.process.strategy.SingleCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class CommandLineParser {

    private final FilterParser filterParser;

    public CommandLineParser() {
        this(new FilterParser());
    }

    public CommandLineParser(FilterParser filterParser) {
        this.filterParser = Objects.requireNonNull(filterParser, "filterParser");
    }

    public CommandRequest parse(String line) {
        List<String> tokens = tokenize(line);
        if (tokens.isEmpty()) {
            return new CommandRequest(List.of(), List.of());
        }

        List<SingleCommand> commands = new ArrayList<>();
        List<Predicate<BoardGame>> filters = new ArrayList<>();

        for (String token : tokens) {
            Optional<Predicate<BoardGame>> filter = filterParser.parse(token);
            if (filter.isPresent()) {
                filters.add(filter.get());
            } else {
                commands.add(parseCommand(token));
            }
        }

        if (commands.isEmpty()) {
            throw new IllegalArgumentException("At least one command is required");
        }
        return new CommandRequest(commands, filters);
    }

    private static SingleCommand parseCommand(String token) {
        try {
            return SingleCommand.valueOf(token.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown command or filter: " + token, e);
        }
    }

    private static List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        if (line == null) {
            return tokens;
        }

        StringBuilder current = new StringBuilder();
        char quote = 0;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (quote != 0) {
                if (character == quote) {
                    quote = 0;
                } else {
                    current.append(character);
                }
            } else if (character == '"' || character == '\'') {
                quote = character;
            } else if (Character.isWhitespace(character)) {
                addToken(tokens, current);
            } else {
                current.append(character);
            }
        }

        if (quote != 0) {
            throw new IllegalArgumentException("Unterminated quote in: " + line);
        }
        addToken(tokens, current);
        return tokens;
    }

    private static void addToken(List<String> tokens, StringBuilder current) {
        if (current.length() > 0) {
            tokens.add(current.toString());
            current.setLength(0);
        }
    }
}
