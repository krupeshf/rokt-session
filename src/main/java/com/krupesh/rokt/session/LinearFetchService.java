package com.krupesh.rokt.session;

import lombok.val;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class LinearFetchService {
  public List<SessionResponse> fetchSessions(String pathToFile, ZonedDateTime fromDateTime,
      ZonedDateTime toDateTime) {

    SessionResponse sessionResponse;

    boolean startFetching = false;

    val sessionResponses = new ArrayList<SessionResponse>();
    try (FileInputStream inputStream = new FileInputStream(pathToFile)) {
      try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);) {
        while (scanner.hasNextLine()) {
          sessionResponse = SessionResponse.fromString(scanner.nextLine());

          if (!startFetching
              && (sessionResponse.getEventTime().isEqual(fromDateTime)
              || (sessionResponse.getEventTime().isAfter(fromDateTime)
              && sessionResponse.getEventTime().isBefore(toDateTime)))) {
            startFetching = true;
          }

          if (startFetching) {
            if (sessionResponse.getEventTime().isAfter(toDateTime)) {
              break;
            }
            sessionResponses.add(sessionResponse);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sessionResponses;
  }
}
