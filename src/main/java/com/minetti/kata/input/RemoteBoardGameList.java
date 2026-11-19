package com.minetti.kata.input;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Loads a board game catalog from a remote HTTP endpoint.
 *
 * <p>Not used by the application, which ships a local catalog, but kept as an alternative
 * {@link BoardGameList} implementation.
 */
public class RemoteBoardGameList implements BoardGameList {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private final String url;

    public RemoteBoardGameList(String url) {
        this.url = url;
    }

    @Override
    public InputStream getInputStream() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
                response.body().close();
                throw new CsvFormatException("HTTP " + response.statusCode() + " for " + url);
            }
            return response.body();
        } catch (IOException e) {
            throw new CsvFormatException("Unable to read CSV from " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CsvFormatException("Interrupted while reading CSV from " + url, e);
        }
    }
}
