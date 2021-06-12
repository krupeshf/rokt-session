package com.krupesh.rokt.session;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class LinearFetchService implements FetchService {

  Logger logger = LoggerFactory.getLogger(FetchService.class);

  @Override
  public List<SessionResponse> fetchSessions(String pathToFile, ZonedDateTime fromDateTime,
      ZonedDateTime toDateTime) throws IOException {

    SessionResponse sessionResponse;

    boolean startFetching = false;

    val sessionResponses = new ArrayList<SessionResponse>();
    try (FileInputStream inputStream = new FileInputStream(pathToFile)) {
      try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);) {
        logger.info("Started reading file {} from date {} to date {}",
            pathToFile, fromDateTime, toDateTime);

        while (scanner.hasNextLine()) {
          sessionResponse = SessionResponse.fromLineEntry(scanner.nextLine());

          if (!startFetching
              && (sessionResponse.getEventTime().isEqual(fromDateTime)
              || sessionResponse.getEventTime().isAfter(fromDateTime))) {
            startFetching = true;
            logger.trace("Started fetching session data");
          }

          if (startFetching) {
            if (sessionResponse.getEventTime().isAfter(toDateTime)) {
              logger.trace("Finished fetching session data");
              break;
            }
            sessionResponses.add(sessionResponse);
          }
        }
      }
    }
    return sessionResponses;
  }
}
