package com.krupesh.rokt.session;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import lombok.val;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@Jacksonized
public class SessionResponse {

  private ZonedDateTime eventTime;
  private String email;
  private UUID sessionId;

  public static SessionResponse fromLineEntry(String plainSessionInfo) {
    val sessionDetails = plainSessionInfo.split(" ");
    return SessionResponse.builder()
        .eventTime(ZonedDateTime.parse(sessionDetails[0]))
        .email(sessionDetails[1])
        .sessionId(UUID.fromString(sessionDetails[2]))
        .build();
  }
}
