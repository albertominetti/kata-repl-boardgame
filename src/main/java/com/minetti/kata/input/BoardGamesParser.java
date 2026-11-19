package com.minetti.kata.input;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.minetti.kata.process.BoardGame;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardGamesParser {

    private static final int GAME_FIELD_COUNT = 7;

    public List<BoardGame> parse(InputStream inputStream) {
        return parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    public List<BoardGame> parseCsv(InputStream inputStream) {
        return parse(inputStream);
    }

    public List<BoardGame> parse(Reader reader) {
        List<BoardGame> games = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .withCSVParser(getCsvParser())
                .build()) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                if (values.length < GAME_FIELD_COUNT) {
                    continue;
                }
                games.add(toBoardGame(values));
            }
            return games;
        } catch (IOException | CsvValidationException e) {
            throw new CsvFormatException("Unable to parse board games CSV", e);
        } catch (NumberFormatException e) {
            throw new CsvFormatException("Invalid number in board games CSV", e);
        }
    }

    private static BoardGame toBoardGame(String[] values) {
        return new BoardGame(
                Integer.parseInt(values[0].trim()),
                values[1].trim(),
                parseTags(values[2]),
                Integer.parseInt(values[3].trim()),
                Integer.parseInt(values[4].trim()),
                new BigDecimal(values[5].trim()),
                values[6].trim());
    }

    private static List<String> parseTags(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        try {
            return Arrays.stream(getTagParser().parseLine(value))
                    .map(String::trim)
                    .filter(tag -> !tag.isEmpty())
                    .toList();
        } catch (IOException e) {
            throw new CsvFormatException("Unable to parse tags: " + value, e);
        }
    }

    private static CSVParser getCsvParser() {
        return new CSVParserBuilder()
                .withSeparator(';')
                .withIgnoreQuotations(false)
                .build();
    }

    private static CSVParser getTagParser() {
        return new CSVParserBuilder()
                .withSeparator(',')
                .build();
    }
}
