package com.krupesh.rokt.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
public class Controller {

  private LinearFetchService fetchSession;

  @Autowired
  public Controller(LinearFetchService fetchSession) {
    this.fetchSession = fetchSession;
  }

  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<SessionResponse> getSessions(@RequestParam String path, @RequestParam String fromDateTime, @RequestParam String toDateTime) {
    return fetchSession.fetchSessions(path,  ZonedDateTime.parse(fromDateTime), ZonedDateTime.parse(toDateTime));
  }
}
