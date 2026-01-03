package net.timelesssdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/** Represents an item in a checkout session. */
public class CheckoutItem {
  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("price")
  private BigDecimal price;

  @JsonProperty("quantity")
  private Integer quantity;

  @JsonProperty("total")
  private BigDecimal total;

  public CheckoutItem() {}

  public CheckoutItem(String name, String description, BigDecimal price, Integer quantity) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.quantity = quantity;
    this.total = price.multiply(BigDecimal.valueOf(quantity));
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }
}
