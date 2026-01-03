package net.timelesssdk.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TimelessPayApiExceptionTest {
  @Test
  void testConstructor() {
    TimelessPayApiException exception = new TimelessPayApiException(400, "Bad request", "ERROR");
    assertEquals(400, exception.getStatusCode());
    assertEquals("Bad request", exception.getErrorMessage());
    assertEquals("ERROR", exception.getErrorStatus());
    assertTrue(exception.getMessage().contains("400"));
    assertTrue(exception.getMessage().contains("Bad request"));
  }

  @Test
  void testGetters() {
    TimelessPayApiException exception = new TimelessPayApiException(401, "Unauthorized", "ERROR");
    assertEquals(401, exception.getStatusCode());
    assertEquals("Unauthorized", exception.getErrorMessage());
    assertEquals("ERROR", exception.getErrorStatus());
  }
}
