package com.minetti.kata.process.strategy;

import com.minetti.kata.process.BoardGame;

import java.util.List;

public class AverageRatingStrategy implements CommandStrategy {

    @Override
    public SingleCommand command() {
        return SingleCommand.AVERAGE_RATING;
    }

    @Override
    public String execute(List<BoardGame> games) {
        double average = games.stream()
                .mapToInt(BoardGame::rating)
                .average()
                .orElse(0);
        return String.valueOf(average);
    }
}
