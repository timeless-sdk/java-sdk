package net.timelesssdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Response model for checkout session creation. */
public class CheckoutSessionResponse {
  @JsonProperty("id")
  private String id;

  @JsonProperty("customerName")
  private String customerName;

  @JsonProperty("customerPhoneNumber")
  private String customerPhoneNumber;

  @JsonProperty("nonce")
  private String nonce;

  @JsonProperty("apiKeyId")
  private Long apiKeyId;

  @JsonProperty("publicKey")
  private String publicKey;

  @JsonProperty("items")
  private List<CheckoutItem> items;

  @JsonProperty("totalAmount")
  private BigDecimal totalAmount;

  @JsonProperty("totalQuantity")
  private Integer totalQuantity;

  @JsonProperty("sessionExpiresAt")
  private LocalDateTime sessionExpiresAt;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("returnUrl")
  private String returnUrl;

  @JsonProperty("createdAt")
  private LocalDateTime createdAt;

  @JsonProperty("updatedAt")
  private LocalDateTime updatedAt;

  public CheckoutSessionResponse() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public Long getApiKeyId() {
    return apiKeyId;
  }

  public void setApiKeyId(Long apiKeyId) {
    this.apiKeyId = apiKeyId;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public List<CheckoutItem> getItems() {
    return items;
  }

  public void setItems(List<CheckoutItem> items) {
    this.items = items;
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
}
