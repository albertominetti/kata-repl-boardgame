package com.minetti.kata.process.strategy;

import com.minetti.kata.process.BoardGame;

import java.util.List;

public interface CommandStrategy {

    SingleCommand command();

    String execute(List<BoardGame> games);
}
