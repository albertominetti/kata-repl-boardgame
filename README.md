# Board Game Catalog Kata

A small Java REPL over a bundled catalog of 151 board games. Type a line of
commands and filters, and get counts and averages back.

The domain is deliberately simple: this is a **kata**, meant for practising
test-driven development, decoupling, and small composable abstractions
(strategy, predicates, ports for input sources).

## Features

- Interactive line-based REPL with quoted arguments.
- Commands: `COUNT`, `AVERAGE_RATING`, `AVERAGE_PLAYTIME`, `EXIT`.
- Filters: title (substring), tag (exact), rating (exact), play time (exact).
- Any number of commands and filters per line, in any order.
- Multiple filters combine with AND and apply to every command on the line.
- Catalog loaded from a CSV resource via OpenCSV, behind a `BoardGameList`
  abstraction (local classpath or HTTP).
- 95 JUnit 5 tests covering parsing, filtering, strategies, and service
  composition.

## Quick start

Requirements: JDK 25 or newer. The Gradle wrapper is included, so no local
Gradle installation is needed.

```bash
git clone https://github.com/albertominetti/kata-repl-boardgame.git
cd kata-repl-boardgame
./gradlew test
./gradlew -q run
```

Type a command and press Enter; leave with `EXIT` or `Ctrl+D`.

To build a self-contained start script instead:

```bash
./gradlew installDist
./build/install/board-games-kata/bin/board-games-kata
```

## Commands

| Command            | Description                              | Example            |
| ------------------ | ---------------------------------------- | ------------------ |
| `COUNT`            | Number of games after filtering          | `FILTER_TAG=Party COUNT` |
| `AVERAGE_RATING`   | Mean rating (1–5) after filtering        | `AVERAGE_RATING`   |
| `AVERAGE_PLAYTIME` | Mean play time in minutes after filtering| `AVERAGE_PLAYTIME` |
| `EXIT`             | Stop the REPL                            | `EXIT`             |

Averages are printed as floating point numbers, e.g. `30.0`; with no matching
games they print `0.0`.

## Filters

| Filter             | Semantics                                | Example                    |
| ------------------ | ---------------------------------------- | -------------------------- |
| `FILTER_TITLE`     | Case-insensitive substring of the title  | `FILTER_TITLE="7 Wonders"` |
| `FILTER_TAG`       | Case-insensitive exact tag match         | `FILTER_TAG=Two Player`    |
| `FILTER_RATING`    | Exact integer rating (1–5)               | `FILTER_RATING=5`          |
| `FILTER_PLAYTIME`  | Exact integer play time in minutes       | `FILTER_PLAYTIME=120`      |

### Rules

- Commands and filters can appear in any order; commands run in the order they
  are written.
- All filters on a line are combined (AND) and applied to every command, even
  when a filter is written after the command: `COUNT FILTER_TAG=Light` filters
  before counting.
- Names are case-insensitive (`count`, `Filter_Tag=party`).
- Values may be wrapped in single or double quotes, which is required for
  values containing spaces: `FILTER_TITLE="Sea Salt & Paper"`.
- Filters support exact values only — comparison operators such as
  `FILTER_RATING>=4` are rejected.
- `EXIT` stops processing, including any commands written after it on the same
  line.
- A blank line produces no output.
- Invalid input (unknown command, unsupported filter, missing or non-numeric
  value, unterminated quote, filters without a command) prints an error
  message and ends the session.

## Example session

User input and program output alternate; the program does not print a prompt.

```text
Welcome to the Board Game Catalog!
COUNT
151
FILTER_TAG=Party COUNT AVERAGE_RATING
36
3.388888888888889
FILTER_TITLE="7 Wonders" AVERAGE_PLAYTIME
30.0
FILTER_PLAYTIME=120 FILTER_RATING=4 COUNT
18
AVERAGE_RATING AVERAGE_PLAYTIME
3.7086092715231787
80.72185430463576
EXIT
```

Commands can also be piped in:

```bash
printf 'FILTER_TAG=Party COUNT\nEXIT\n' | ./gradlew -q run
```

## Catalog format

The bundled catalog lives at `src/main/resources/boardgames.csv`. It is
semicolon-separated and its header row is skipped:

```text
ID;Title;Tags;Rating;PlayTime;Price;Description
173346;7 Wonders Duel;Base Game, Two Player, Medium;5;30;19.90;BGG rank #24. Community rating 8.1/10.
```

- `Tags` is a comma-separated list.
- `Rating` is an integer from 1 to 5.
- `PlayTime` is an integer number of minutes.
- `Price` is parsed as a decimal, but is not currently used by any command or
  filter.
- Rows with fewer than seven columns are skipped; surrounding whitespace is
  trimmed.

The data is derived from BoardGameGeek and is included only as kata material.

## Design notes

- **Separation of input and processing.** `BoardGameList` hides the catalog
  source; `BoardGamesParser` hides CSV details; `CommandLineParser` turns text
  into a `CommandRequest` of commands plus game predicates. The processing
  layer knows nothing about CSV or the terminal.
- **Strategy pattern for behaviour.** `CommandStrategy` and `FilterStrategy`
  each have a single responsibility. `DefaultCommandStrategies` and
  `DefaultFilterStrategies` are the registries used by `Main`.
- **Immutable data.** `BoardGame`, `CommandRequest`, and `CommandResult` are
  records with defensive copies.
- **Extending it.** To add a command, implement `CommandStrategy`, add a
  `SingleCommand` enum value, and register it in `DefaultCommandStrategies`.
  To add a filter, implement `FilterStrategy`, add a `SingleFilter` enum value,
  and register it in `DefaultFilterStrategies`.

### Known limitations

- `RemoteBoardGameList` exists as an alternative implementation but is not
  selected by `Main`.
- An error ends the REPL because the catch block wraps the whole loop.
- Filters are exact-match only (title is the exception, using substring).

## Tests

```bash
./gradlew test
```

## Possible kata exercises

- Add `FILTER_PRICE` and range operators (`>=`, `<=`) instead of exact values.
- Add sorting or top-N commands, and a command that lists titles.
- Return play-time ranges (e.g. `FILTER_PLAYTIME=60-120`).
- Keep the REPL alive after an invalid command and report errors per line.
- Wire in `RemoteBoardGameList` and make the source configurable from the CLI.
- Improve number formatting (e.g. two decimal places) behind a formatter
  abstraction.

## Tech stack

Java 25, Gradle 9.1 (wrapper), OpenCSV 5.12.0, JUnit 5.12.2.

## License

Released under the [MIT License](LICENSE).
