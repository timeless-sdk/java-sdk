package net.timelesssdk.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Webhook payload model for all event types. */
public class WebhookPayload {
  @JsonProperty("event_type")
  private String eventType;

  @JsonProperty("webhook_id")
  private String webhookId;

  @JsonProperty("timestamp")
  private LocalDateTime timestamp;

  @JsonProperty("session_id")
  private String sessionId;

  @JsonProperty("payment_id")
  private Long paymentId;

  @JsonProperty("status")
  private String status;

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("customer_name")
  private String customerName;

  @JsonProperty("customer_phone")
  private String customerPhone;

  @JsonProperty("items")
  private List<WebhookItem> items;

  @JsonProperty("metadata")
  private WebhookMetadata metadata;

  public WebhookPayload() {}

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getWebhookId() {
    return webhookId;
  }

  public void setWebhookId(String webhookId) {
    this.webhookId = webhookId;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public Long getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerPhone() {
    return customerPhone;
  }

  public void setCustomerPhone(String customerPhone) {
    this.customerPhone = customerPhone;
  }

  public List<WebhookItem> getItems() {
    return items;
  }

  public void setItems(List<WebhookItem> items) {
    this.items = items;
  }

  public WebhookMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(WebhookMetadata metadata) {
    this.metadata = metadata;
  }

  /** Event type constants. */
  public static class EventType {
    public static final String PAYMENT_SUCCESS = "payment.success";
    public static final String PAYMENT_FAILED = "payment.failed";
    public static final String PAYMENT_PENDING = "payment.pending";
    public static final String PAYMENT_CANCELLED = "payment.cancelled";
    public static final String PAYMENT_REFUNDED = "payment.refunded";
    public static final String SESSION_CREATED = "session.created";

    private EventType() {}
  }
}
