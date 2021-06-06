package com.krupesh.rokt.session;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(Controller.class)
public class ControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LinearFetchService mockService;

  @Test
  public void givenValidRequest_returnsValidResponse() throws Exception {

    when(mockService.fetchSessions("src/test/resources/sample1.txt",
        ZonedDateTime.parse("2000-01-01T23:59:04Z"),
        ZonedDateTime.parse("2000-01-01T23:59:05Z")))
        .thenReturn(Collections.singletonList(SessionResponse.fromLineEntry(
            "2000-01-01T23:59:04Z abner@bartolettihills.com b3daf720-6112-4a49-9895-62dda13a2932")));

    this.mockMvc.perform(
        get("/")
            .param("path", "src/test/resources/sample1.txt")
            .param("fromDateTime", "2000-01-01T23:59:04Z")
            .param("toDateTime", "2000-01-01T23:59:05Z")
    ).andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].eventTime", is("2000-01-01T23:59:04Z")))
        .andExpect(jsonPath("$[0].email", is("abner@bartolettihills.com")))
        .andExpect(jsonPath("$[0].sessionId", is("b3daf720-6112-4a49-9895-62dda13a2932")))
    ;
  }
}
