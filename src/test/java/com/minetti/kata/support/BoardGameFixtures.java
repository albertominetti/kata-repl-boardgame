package com.minetti.kata.support;

import com.minetti.kata.process.BoardGame;
import java.math.BigDecimal;
import java.util.List;

public final class BoardGameFixtures {

    public static final BoardGame SEA_SALT_AND_PAPER = new BoardGame(
            367220,
            "Sea Salt & Paper",
            List.of("Base Game", "Multiplayer", "Light"),
            5,
            45,
            new BigDecimal("18.47"),
            "Collect cards and trigger combos.");

    public static final BoardGame BRASS_BIRMINGHAM = new BoardGame(
            224517,
            "Brass: Birmingham",
            List.of("Base Game", "Multiplayer", "Heavy"),
            4,
            120,
            new BigDecimal("35.43"),
            "Build industries across the Midlands.");

    public static final List<BoardGame> GAMES = List.of(SEA_SALT_AND_PAPER, BRASS_BIRMINGHAM);

    private BoardGameFixtures() {
    }

    public static BoardGame withTitle(String title) {
        return new BoardGame(0, title, List.of(), 0, 0, BigDecimal.ZERO, "");
    }
}
