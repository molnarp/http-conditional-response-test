package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FruitResourceTest {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).withZone(ZoneId.of("GMT"));

    WebTestClient client;

    @BeforeEach
    void setup() {
        client = MockMvcWebTestClient.bindToController(new FruitResource()).build();
    }

    @Test
    void getsFruit() {
        client.get()
                .uri("/fruits/Apple")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().lastModified(Fruit.LAST_MODIFIED.toEpochMilli())
                .expectHeader().valueEquals(HttpHeaders.ETAG, "\"Apple\"");
    }

    // THIS TEST IS FAILING
    @Test
    void getsFruitIfUnmodifiedSincePass() {
        client.get()
                .uri("/fruits/Apple")
                .header(HttpHeaders.IF_UNMODIFIED_SINCE, DATE_FORMATTER.format(Fruit.LAST_MODIFIED.plusSeconds(3600)))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().lastModified(Fruit.LAST_MODIFIED.toEpochMilli())
                .expectHeader().valueEquals(HttpHeaders.ETAG, "\"Apple\"");
    }

    @Test
    void getsFruitIfUnmodifiedSinceFail() {
        client.get()
                .uri("/fruits/Apple")
                .header(HttpHeaders.IF_UNMODIFIED_SINCE, DATE_FORMATTER.format(Fruit.LAST_MODIFIED.minusSeconds(3600)))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.PRECONDITION_FAILED);
    }

}
