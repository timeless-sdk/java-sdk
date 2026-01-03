package net.timelesssdk.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TimelessPayExceptionTest {
  @Test
  void testConstructorWithMessage() {
    TimelessPayException exception = new TimelessPayException("Test error");
    assertEquals("Test error", exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testConstructorWithMessageAndCause() {
    Throwable cause = new RuntimeException("Root cause");
    TimelessPayException exception = new TimelessPayException("Test error", cause);
    assertEquals("Test error", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}
