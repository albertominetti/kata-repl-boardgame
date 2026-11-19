package com.minetti.kata.process.strategy;

import com.minetti.kata.process.BoardGame;

import java.util.List;

public class CountStrategy implements CommandStrategy {

    @Override
    public SingleCommand command() {
        return SingleCommand.COUNT;
    }

    @Override
    public String execute(List<BoardGame> games) {
        return String.valueOf(games.size());
    }
}
