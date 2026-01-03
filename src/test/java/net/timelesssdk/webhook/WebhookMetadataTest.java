package net.timelesssdk.webhook;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class WebhookMetadataTest {
  @Test
  void testDefaultConstructor() {
    WebhookMetadata metadata = new WebhookMetadata();
    assertNotNull(metadata);
  }

  @Test
  void testGettersAndSetters() {
    WebhookMetadata metadata = new WebhookMetadata();
    metadata.setTransactionId(12345L);
    metadata.setPaymentMethod("CBEBIRR");
    metadata.setTotalAmount(new BigDecimal("200.00"));
    metadata.setCommission(new BigDecimal("5.00"));
    metadata.setBusinessId("business-uuid");
    metadata.setCreatedAt(LocalDateTime.now());
    metadata.setUpdatedAt(LocalDateTime.now());
    metadata.setSessionId("session-id-123");
    metadata.setApiKeyId(123L);
    metadata.setTotalQuantity(2);
    metadata.setSessionExpiresAt(LocalDateTime.now());

    assertEquals(12345L, metadata.getTransactionId());
    assertEquals("CBEBIRR", metadata.getPaymentMethod());
    assertEquals(new BigDecimal("200.00"), metadata.getTotalAmount());
    assertEquals(new BigDecimal("5.00"), metadata.getCommission());
    assertEquals("business-uuid", metadata.getBusinessId());
    assertNotNull(metadata.getCreatedAt());
    assertNotNull(metadata.getUpdatedAt());
    assertEquals("session-id-123", metadata.getSessionId());
    assertEquals(123L, metadata.getApiKeyId());
    assertEquals(2, metadata.getTotalQuantity());
    assertNotNull(metadata.getSessionExpiresAt());
  }
}
