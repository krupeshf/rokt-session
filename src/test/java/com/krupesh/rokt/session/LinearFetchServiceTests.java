package com.krupesh.rokt.session;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LinearFetchServiceTests {

  @TestConfiguration
  static class LinearFetchServiceTestContextConfiguration {

    @Bean
    public FetchService fetchService() {
      return new LinearFetchService();
    }
  }

  @Autowired
  private FetchService fetchService;

  @Test
  void givenValidRequest_returnAllSessions() throws IOException {
    final List<SessionResponse> sessionResponses =
        fetchService.fetchSessions(TestUtils.SAMPLE_FILE_LOCATION,
            ZonedDateTime.parse("2000-01-06T06:27:36Z"),
            ZonedDateTime.parse("2000-01-06T06:27:37Z"));

    assertEquals(1, sessionResponses.size());
    assertEquals("2000-01-06T06:27:36Z",
        sessionResponses.get(0).getEventTime().format(DateTimeFormatter.ISO_INSTANT));
  }

  @Test
  void givenVeryRecentToDate_returnValidResults() throws IOException {
    final List<SessionResponse> sessionResponses =
        fetchService.fetchSessions(TestUtils.SAMPLE_FILE_LOCATION,
            ZonedDateTime.parse("2000-01-01T23:59:04Z"),
            ZonedDateTime.parse("2020-01-06T06:27:36Z"));
    assertEquals(9, sessionResponses.size());
  }

  @Test
  void givenVeryOldFromDate_returnValidResults() throws IOException {
    final List<SessionResponse> sessionResponses =
        fetchService.fetchSessions(TestUtils.SAMPLE_FILE_LOCATION,
            ZonedDateTime.parse("1990-01-01T23:59:04Z"),
            ZonedDateTime.parse("2000-01-02T20:59:05Z"));
    assertEquals(3, sessionResponses.size());
  }

  @Test
  void givenVeryOldFromDateAndOldToDate_returnZeroResults() throws IOException {
    final List<SessionResponse> sessionResponses =
        fetchService.fetchSessions(TestUtils.SAMPLE_FILE_LOCATION,
            ZonedDateTime.parse("1990-01-01T23:59:04Z"),
            ZonedDateTime.parse("1990-01-06T06:27:36Z"));
    assertEquals(0, sessionResponses.size());
  }

  @Test
  void givenVeryNewFromDateAndNewToDate_returnZeroResults() throws IOException {
    final List<SessionResponse> sessionResponses =
        fetchService.fetchSessions(TestUtils.SAMPLE_FILE_LOCATION,
            ZonedDateTime.parse("1990-01-01T23:59:04Z"),
            ZonedDateTime.parse("1990-01-06T06:27:36Z"));
    assertEquals(0, sessionResponses.size());
  }
}
