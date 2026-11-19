package com.minetti.kata.input;

import java.io.InputStream;

public class LocalBoardGameList implements BoardGameList {

    private final String resource;

    public LocalBoardGameList(String resource) {
        this.resource = resource.startsWith("/") ? resource : "/" + resource;
    }

    @Override
    public InputStream getInputStream() {
        InputStream inputStream = LocalBoardGameList.class.getResourceAsStream(resource);
        if (inputStream == null) {
            throw new CsvFormatException("Resource not found: " + resource);
        }
        return inputStream;
    }
}
