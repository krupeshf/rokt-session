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

    val sessionResponses = new ArrayList<SessionResponse>();
    try (FileInputStream inputStream = new FileInputStream(pathToFile)) {
      try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);) {
        while (scanner.hasNextLine()) {
          sessionResponses.add(SessionResponse.fromString(scanner.nextLine()));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sessionResponses;
  }
}
