package com.krupesh.rokt.session;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIntegrationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @Test
  public void givenValidRequest_returnsResults() throws IOException {

    final String response = this.restTemplate.getForObject("http://localhost:" + port
            + "/?path=src/test/resources/sample1.txt&fromDateTime=2000-01-03T16:13:52Z&toDateTime=2000-01-03T16:13:53Z",
        String.class);

    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    JsonNode node = objectMapper.readTree(response);
    ObjectReader reader = objectMapper.readerFor(new TypeReference<List<SessionResponse>>() {});

    List<SessionResponse> list = reader.readValue(node);

    assertEquals(1, list.size());
    assertEquals("2000-01-03T16:13:52Z", list.get(0).getEventTime().format(DateTimeFormatter.ISO_INSTANT));
    assertEquals("clotilde@nolanbalistreri.uk", list.get(0).getEmail());
    assertEquals("8be575ca-2fa6-43d3-bf69-608b70c8be18", list.get(0).getSessionId().toString());
  }
}
