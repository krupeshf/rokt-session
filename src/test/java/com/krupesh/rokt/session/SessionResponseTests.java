package com.krupesh.rokt.session;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.*;

public class SessionResponseTests {
  @Test
  public void givenValidLine_parseSession() {
    SessionResponse response = SessionResponse.fromString(
        "2020-12-04T11:14:23Z jane.doe@email.com 2f31eb2c-a735-4c91-a122-b3851bc87355");
    assertEquals("2020-12-04T11:14:23Z", response.getEventTime().toString());
    assertEquals("jane.doe@email.com", response.getEmail());
    assertEquals("2f31eb2c-a735-4c91-a122-b3851bc87355", response.getSessionId().toString());
  }

  @Test
  public void givenInvalidDate_throwDateTimeParseException() {
    assertThrows(DateTimeException.class, () -> {
      SessionResponse.fromString(
          "2020-13-04T11:14:23Z jane.doe@email.com 2f31eb2c-a735-4c91-a122-b3851bc87355");
    });
  }

  @Test
  //ToDo: we should have email validation
  public void givenInvalidEmail_acceptIt() {
    assertDoesNotThrow(() -> {
      SessionResponse.fromString(
          "2020-12-04T11:14:23Z invalid.email 2f31eb2c-a735-4c91-a122-b3851bc87355");
    });
  }

  @Test
  public void givenInvalidSessionid_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      SessionResponse.fromString("2020-12-04T11:14:23Z jane.doe@email.com f31eb2c-b3851bc87355");
    });
  }
}
