package net.timelesssdk.webhook;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class WebhookItemTest {
  @Test
  void testDefaultConstructor() {
    WebhookItem item = new WebhookItem();
    assertNotNull(item);
  }

  @Test
  void testGettersAndSetters() {
    WebhookItem item = new WebhookItem();
    item.setTotalQuantity(2);
    item.setTotalAmount(new BigDecimal("200.00"));

    assertEquals(2, item.getTotalQuantity());
    assertEquals(new BigDecimal("200.00"), item.getTotalAmount());
  }
}
