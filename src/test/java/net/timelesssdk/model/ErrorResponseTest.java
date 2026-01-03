package net.timelesssdk.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {
  @Test
  void testDefaultConstructor() {
    ErrorResponse response = new ErrorResponse();
    assertNotNull(response);
  }

  @Test
  void testGettersAndSetters() {
    ErrorResponse response = new ErrorResponse();
    response.setMessage("Error message");
    response.setStatus("ERROR");

    assertEquals("Error message", response.getMessage());
    assertEquals("ERROR", response.getStatus());
  }
}
