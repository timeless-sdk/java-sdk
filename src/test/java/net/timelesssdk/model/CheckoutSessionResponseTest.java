package net.timelesssdk.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CheckoutSessionResponseTest {
  @Test
  void testDefaultConstructor() {
    CheckoutSessionResponse response = new CheckoutSessionResponse();
    assertNotNull(response);
  }

  @Test
  void testGettersAndSetters() {
    CheckoutSessionResponse response = new CheckoutSessionResponse();
    response.setId("session-id-123");
    response.setCustomerName("John Doe");
    response.setCustomerPhoneNumber("+251911234567");
    response.setNonce("test-nonce");
    response.setApiKeyId(123L);
    response.setPublicKey("public-key");
    response.setCurrency("ETB");
    response.setReturnUrl("https://example.com/return");
    response.setTotalAmount(new BigDecimal("200.00"));
    response.setTotalQuantity(2);
    response.setSessionExpiresAt(LocalDateTime.now());
    response.setCreatedAt(LocalDateTime.now());
    response.setUpdatedAt(LocalDateTime.now());

    CheckoutItem item = new CheckoutItem();
    List<CheckoutItem> items = new ArrayList<>();
    items.add(item);
    response.setItems(items);

    assertEquals("session-id-123", response.getId());
    assertEquals("John Doe", response.getCustomerName());
    assertEquals("+251911234567", response.getCustomerPhoneNumber());
    assertEquals("test-nonce", response.getNonce());
    assertEquals(123L, response.getApiKeyId());
    assertEquals("public-key", response.getPublicKey());
    assertEquals("ETB", response.getCurrency());
    assertEquals("https://example.com/return", response.getReturnUrl());
    assertEquals(new BigDecimal("200.00"), response.getTotalAmount());
    assertEquals(2, response.getTotalQuantity());
    assertNotNull(response.getSessionExpiresAt());
    assertNotNull(response.getCreatedAt());
    assertNotNull(response.getUpdatedAt());
    assertEquals(1, response.getItems().size());
  }
}
