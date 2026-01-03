package net.timelesssdk.exception;

/** Base exception for TimelessPay SDK errors. */
public class TimelessPayException extends Exception {
  public TimelessPayException(String message) {
    super(message);
  }

  public TimelessPayException(String message, Throwable cause) {
    super(message, cause);
  }
}
