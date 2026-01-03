package net.timelesssdk.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SignatureExceptionTest {
  @Test
  void testConstructorWithMessage() {
    SignatureException exception = new SignatureException("Signature error");
    assertEquals("Signature error", exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testConstructorWithMessageAndCause() {
    Throwable cause = new RuntimeException("Root cause");
    SignatureException exception = new SignatureException("Signature error", cause);
    assertEquals("Signature error", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}
