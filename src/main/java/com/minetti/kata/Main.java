package com.minetti.kata;

import com.minetti.kata.input.BoardGameList;
import com.minetti.kata.input.CommandLineParser;
import com.minetti.kata.input.CommandRequest;
import com.minetti.kata.input.LocalBoardGameList;
import com.minetti.kata.input.BoardGamesParser;
import com.minetti.kata.process.BoardGame;
import com.minetti.kata.process.BoardGameService;
import com.minetti.kata.process.strategy.DefaultCommandStrategies;
import com.minetti.kata.process.strategy.SingleCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    private static final String CATALOG_RESOURCE = "/boardgames.csv";

    public static void main(String[] args) {
        System.out.println("Welcome to the Board Game Catalog!");

        BoardGameList boardGameList = new LocalBoardGameList(CATALOG_RESOURCE);
        try (InputStream inputStream = boardGameList.getInputStream()) {
            BoardGamesParser parser = new BoardGamesParser();
            List<BoardGame> games = parser.parse(inputStream);
            runRepl(games);
        } catch (IOException e) {
            throw new RuntimeException("Unable to close the board games input stream", e);
        }
    }

    private static void runRepl(List<BoardGame> games) {
        CommandLineParser commandLineParser = new CommandLineParser();
        BoardGameService gameService = new BoardGameService(DefaultCommandStrategies.create());

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            CommandRequest request;

            while ((line = reader.readLine()) != null
                    && !(request = commandLineParser.parse(line)).commands().contains(SingleCommand.EXIT)) {
                CommandResult result = gameService.execute(request, games);
                result.results().forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read commands from standard input", e);
        }
    }
}
