package net.timelesssdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Error response model from the API. */
public class ErrorResponse {
  @JsonProperty("message")
  private String message;

  @JsonProperty("status")
  private String status;

  public ErrorResponse() {}

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
