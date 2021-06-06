package com.krupesh.rokt.session;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
public class ControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LinearFetchService mockService;

  @Test
  public void givenValidRequest_returnsValidResponse() throws Exception {
    when(mockService.fetchSessions(TestUtils.SAMPLE_FILE_LOCATION,
        ZonedDateTime.parse("2000-01-01T23:59:04Z"),
        ZonedDateTime.parse("2000-01-01T23:59:05Z")))
        .thenReturn(Collections.singletonList(SessionResponse.fromLineEntry(
            "2000-01-01T23:59:04Z abner@bartolettihills.com b3daf720-6112-4a49-9895-62dda13a2932")));

    this.mockMvc.perform(
        get("/")
            .param("path", TestUtils.SAMPLE_FILE_LOCATION)
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

  @Test
  void givenInvalidRequestParam_returnMissingServletRequestParameterException() throws Exception {
    this.mockMvc.perform(
        get("/")
            .param("path", TestUtils.SAMPLE_FILE_LOCATION)
            .param("invalidFromDateTimeRequestParam", "2000-01-01T23:59:04Z")
            .param("toDateTime", "2000-01-01T23:59:05Z")
    )
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(
            result.getResolvedException() instanceof MissingServletRequestParameterException))
    ;
  }

  @Test
  void givenInvalidFromDate_returnMissingServletRequestParameterException() throws Exception {
    this.mockMvc.perform(
        get("/")
            .param("path", TestUtils.SAMPLE_FILE_LOCATION)
            .param("fromDateTime", "2000-13-01T23:59:05Z")
            .param("toDateTime", "2000-01-01T23:59:05Z")
    )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(
            result.getResolvedException() instanceof DateTimeParseException))
    ;
  }

  @Test
  void givenInvalidPath_returnException() throws Exception {
    when(mockService.fetchSessions(eq("invalidPath.txt"), any(), any())).thenCallRealMethod();

    this.mockMvc.perform(
        get("/")
            .param("path", "invalidPath.txt")
            .param("fromDateTime", "2000-01-01T23:59:05Z")
            .param("toDateTime", "2000-01-01T23:59:05Z")
    )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(
            result.getResolvedException() instanceof IOException))
    ;
  }
}
