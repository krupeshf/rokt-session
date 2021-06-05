package com.krupesh.rokt.session;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SessionResponseTest {
  @Test
  public void parseSessionDetails() {
    SessionResponse response = SessionResponse.fromString("2020-12-04T11:14:23Z jane.doe@email.com 2f31eb2c-a735-4c91-a122-b3851bc87355");
    Assertions.assertEquals("2020-12-04T11:14:23Z", response.getEventTime().toString());
    Assertions.assertEquals("jane.doe@email.com", response.getEmail());
    Assertions.assertEquals("2f31eb2c-a735-4c91-a122-b3851bc87355", response.getSessionId().toString());
  }
}
