package net.timelesssdk.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/** Item information in webhook payload. */
public class WebhookItem {
  @JsonProperty("total_quantity")
  private Integer totalQuantity;

  @JsonProperty("total_amount")
  private BigDecimal totalAmount;

  public WebhookItem() {}

  public Integer getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(Integer totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }
}
