package com.krupesh.rokt.session;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

public interface FetchService {
  List<SessionResponse> fetchSessions(String pathToFile, ZonedDateTime fromDateTime,
      ZonedDateTime toDateTime) throws IOException;
}
