package net.timelesssdk.exception;

/** Exception thrown when signature generation or verification fails. */
public class SignatureException extends TimelessPayException {
  public SignatureException(String message) {
    super(message);
  }

  public SignatureException(String message, Throwable cause) {
    super(message, cause);
  }
}
