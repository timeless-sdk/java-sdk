package net.timelesssdk.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CheckoutItemTest {
  @Test
  void testDefaultConstructor() {
    CheckoutItem item = new CheckoutItem();
    assertNotNull(item);
  }

  @Test
  void testConstructorWithParameters() {
    CheckoutItem item = new CheckoutItem("Product 1", "Description", new BigDecimal("100.00"), 2);
    assertEquals("Product 1", item.getName());
    assertEquals("Description", item.getDescription());
    assertEquals(new BigDecimal("100.00"), item.getPrice());
    assertEquals(2, item.getQuantity());
    assertEquals(new BigDecimal("200.00"), item.getTotal());
  }

  @Test
  void testGettersAndSetters() {
    CheckoutItem item = new CheckoutItem();
    item.setName("Test Product");
    item.setDescription("Test Description");
    item.setPrice(new BigDecimal("50.00"));
    item.setQuantity(3);
    item.setTotal(new BigDecimal("150.00"));

    assertEquals("Test Product", item.getName());
    assertEquals("Test Description", item.getDescription());
    assertEquals(new BigDecimal("50.00"), item.getPrice());
    assertEquals(3, item.getQuantity());
    assertEquals(new BigDecimal("150.00"), item.getTotal());
  }

  @Test
  void testTotalCalculation() {
    CheckoutItem item = new CheckoutItem("Product", "Desc", new BigDecimal("25.50"), 4);
    assertEquals(new BigDecimal("102.00"), item.getTotal());
  }
}
