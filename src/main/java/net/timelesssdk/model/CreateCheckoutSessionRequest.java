package net.timelesssdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Request model for creating a checkout session. */
public class CreateCheckoutSessionRequest {
  @JsonProperty("customerName")
  private String customerName;

  @JsonProperty("customerPhoneNumber")
  private String customerPhoneNumber;

  @JsonProperty("nonce")
  private String nonce;

  @JsonProperty("items")
  private List<CheckoutItem> items;

  @JsonProperty("sessionExpiresAt")
  private LocalDateTime sessionExpiresAt;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("returnUrl")
  private String returnUrl;

  @JsonProperty("totalAmount")
  private BigDecimal totalAmount;

  @JsonProperty("totalQuantity")
  private Integer totalQuantity;

  @JsonProperty("correlationId")
  private String correlationId;

  public CreateCheckoutSessionRequest() {}

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerPhoneNumber() {
    return customerPhoneNumber;
  }

  public void setCustomerPhoneNumber(String customerPhoneNumber) {
    this.customerPhoneNumber = customerPhoneNumber;
  }

  public String getNonce() {
    return nonce;
  }

  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

  public List<CheckoutItem> getItems() {
    return items;
  }

  public void setItems(List<CheckoutItem> items) {
    this.items = items;
  }

  public LocalDateTime getSessionExpiresAt() {
    return sessionExpiresAt;
  }

  public void setSessionExpiresAt(LocalDateTime sessionExpiresAt) {
    this.sessionExpiresAt = sessionExpiresAt;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getReturnUrl() {
    return returnUrl;
  }

  public void setReturnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Integer getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(Integer totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }
}
