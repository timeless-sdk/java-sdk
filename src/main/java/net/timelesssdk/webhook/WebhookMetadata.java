package net.timelesssdk.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Metadata in webhook payload. Structure varies by event type. */
public class WebhookMetadata {
  @JsonProperty("transaction_id")
  private Long transactionId;

  @JsonProperty("payment_method")
  private String paymentMethod;

  @JsonProperty("total_amount")
  private BigDecimal totalAmount;

  @JsonProperty("commission")
  private BigDecimal commission;

  @JsonProperty("business_id")
  private String businessId;

  @JsonProperty("created_at")
  private LocalDateTime createdAt;

  @JsonProperty("updated_at")
  private LocalDateTime updatedAt;

  // For session.created events
  @JsonProperty("session_id")
  private String sessionId;

  @JsonProperty("api_key_id")
  private Long apiKeyId;

  @JsonProperty("total_quantity")
  private Integer totalQuantity;

  @JsonProperty("session_expires_at")
  private LocalDateTime sessionExpiresAt;

  public WebhookMetadata() {}

  public Long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(Long transactionId) {
    this.transactionId = transactionId;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public BigDecimal getCommission() {
    return commission;
  }

  public void setCommission(BigDecimal commission) {
    this.commission = commission;
  }

  public String getBusinessId() {
    return businessId;
  }

  public void setBusinessId(String businessId) {
    this.businessId = businessId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public Long getApiKeyId() {
    return apiKeyId;
  }

  public void setApiKeyId(Long apiKeyId) {
    this.apiKeyId = apiKeyId;
  }

  public Integer getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(Integer totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public LocalDateTime getSessionExpiresAt() {
    return sessionExpiresAt;
  }

  public void setSessionExpiresAt(LocalDateTime sessionExpiresAt) {
    this.sessionExpiresAt = sessionExpiresAt;
  }
}
