package net.timelesssdk.webhook;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class WebhookPayloadTest {
  @Test
  void testDefaultConstructor() {
    WebhookPayload payload = new WebhookPayload();
    assertNotNull(payload);
  }

  @Test
  void testGettersAndSetters() {
    WebhookPayload payload = new WebhookPayload();
    payload.setEventType("payment.success");
    payload.setWebhookId("webhook-id-123");
    payload.setTimestamp(LocalDateTime.now());
    payload.setSessionId("session-id-123");
    payload.setPaymentId(12345L);
    payload.setStatus("success");
    payload.setAmount(new BigDecimal("200.00"));
    payload.setCurrency("ETB");
    payload.setCustomerName("John Doe");
    payload.setCustomerPhone("+251911234567");

    WebhookItem item = new WebhookItem();
    List<WebhookItem> items = new ArrayList<>();
    items.add(item);
    payload.setItems(items);

    WebhookMetadata metadata = new WebhookMetadata();
    payload.setMetadata(metadata);

    assertEquals("payment.success", payload.getEventType());
    assertEquals("webhook-id-123", payload.getWebhookId());
    assertNotNull(payload.getTimestamp());
    assertEquals("session-id-123", payload.getSessionId());
    assertEquals(12345L, payload.getPaymentId());
    assertEquals("success", payload.getStatus());
    assertEquals(new BigDecimal("200.00"), payload.getAmount());
    assertEquals("ETB", payload.getCurrency());
    assertEquals("John Doe", payload.getCustomerName());
    assertEquals("+251911234567", payload.getCustomerPhone());
    assertEquals(1, payload.getItems().size());
    assertNotNull(payload.getMetadata());
  }

  @Test
  void testEventTypeConstants() {
    assertEquals("payment.success", WebhookPayload.EventType.PAYMENT_SUCCESS);
    assertEquals("payment.failed", WebhookPayload.EventType.PAYMENT_FAILED);
    assertEquals("payment.pending", WebhookPayload.EventType.PAYMENT_PENDING);
    assertEquals("payment.cancelled", WebhookPayload.EventType.PAYMENT_CANCELLED);
    assertEquals("payment.refunded", WebhookPayload.EventType.PAYMENT_REFUNDED);
    assertEquals("session.created", WebhookPayload.EventType.SESSION_CREATED);
  }
}
