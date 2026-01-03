package net.timelesssdk.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CreateCheckoutSessionRequestTest {
  @Test
  void testDefaultConstructor() {
    CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest();
    assertNotNull(request);
  }

  @Test
  void testGettersAndSetters() {
    CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest();
    request.setCustomerName("John Doe");
    request.setCustomerPhoneNumber("+251911234567");
    request.setNonce("test-nonce");
    request.setCurrency("ETB");
    request.setSessionExpiresAt(LocalDateTime.now());
    request.setReturnUrl("https://example.com/return");
    request.setTotalAmount(new BigDecimal("200.00"));
    request.setTotalQuantity(2);
    request.setCorrelationId("correlation-123");

    CheckoutItem item = new CheckoutItem();
    item.setName("Product 1");
    List<CheckoutItem> items = new ArrayList<>();
    items.add(item);
    request.setItems(items);

    assertEquals("John Doe", request.getCustomerName());
    assertEquals("+251911234567", request.getCustomerPhoneNumber());
    assertEquals("test-nonce", request.getNonce());
    assertEquals("ETB", request.getCurrency());
    assertNotNull(request.getSessionExpiresAt());
    assertEquals("https://example.com/return", request.getReturnUrl());
    assertEquals(new BigDecimal("200.00"), request.getTotalAmount());
    assertEquals(2, request.getTotalQuantity());
    assertEquals("correlation-123", request.getCorrelationId());
    assertEquals(1, request.getItems().size());
  }
}
