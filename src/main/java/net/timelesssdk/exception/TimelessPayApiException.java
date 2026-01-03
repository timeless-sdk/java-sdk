package net.timelesssdk.exception;

/** Exception thrown when API returns an error response. */
public class TimelessPayApiException extends TimelessPayException {
  private final int statusCode;
  private final String errorMessage;
  private final String errorStatus;

  public TimelessPayApiException(int statusCode, String errorMessage, String errorStatus) {
    super(String.format("API error [%d]: %s", statusCode, errorMessage));
    this.statusCode = statusCode;
    this.errorMessage = errorMessage;
    this.errorStatus = errorStatus;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public String getErrorStatus() {
    return errorStatus;
  }
}
