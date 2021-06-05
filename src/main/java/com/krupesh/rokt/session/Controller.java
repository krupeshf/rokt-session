package com.krupesh.rokt.session;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class Controller {

  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<SessionResponse> getSessions() {
    return Collections.singletonList(SessionResponse.fromString("2020-12-04T11:14:23Z jane.doe@email.com 2f31eb2c-a735-4c91-a122-b3851bc87355"));
  }
}
