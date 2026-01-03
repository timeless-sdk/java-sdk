package net.timelesssdk.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.security.PublicKey;
import net.timelesssdk.exception.SignatureException;
import net.timelesssdk.exception.TimelessPayException;
import net.timelesssdk.security.SignatureUtil;

/** Utility class for handling webhook notifications from TimelessPay. */
public class WebhookHandler {
  private final PublicKey publicKey;
  private final ObjectMapper objectMapper;

  /**
   * Creates a new WebhookHandler.
   *
   * @param publicKey The public key for verifying webhook signatures (Base64-encoded X.509 format)
   * @throws TimelessPayException if public key loading fails
   */
  public WebhookHandler(String publicKey) throws TimelessPayException {
    try {
      this.publicKey = SignatureUtil.loadPublicKey(publicKey);
      this.objectMapper = new ObjectMapper();
      this.objectMapper.registerModule(new JavaTimeModule());
    } catch (SignatureException e) {
      throw new TimelessPayException("Failed to load public key", e);
    }
  }

  /**
   * Creates a new WebhookHandler.
   *
   * @param publicKey The public key for verifying webhook signatures
   */
  public WebhookHandler(PublicKey publicKey) {
    this.publicKey = publicKey;
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  /**
   * Verifies the webhook signature.
   *
   * @param payloadJson The webhook payload as JSON string
   * @param signature The Base64-encoded signature from X-Payload-Signature header
   * @return true if signature is valid, false otherwise
   * @throws SignatureException if verification fails
   */
  public boolean verifySignature(String payloadJson, String signature) throws SignatureException {
    return SignatureUtil.verify(payloadJson, signature, publicKey);
  }

  /**
   * Parses the webhook payload from JSON string.
   *
   * @param payloadJson The webhook payload as JSON string
   * @return WebhookPayload object
   * @throws TimelessPayException if parsing fails
   */
  public WebhookPayload parsePayload(String payloadJson) throws TimelessPayException {
    try {
      return objectMapper.readValue(payloadJson, WebhookPayload.class);
    } catch (Exception e) {
      throw new TimelessPayException("Failed to parse webhook payload", e);
    }
  }

  /**
   * Verifies and parses a webhook payload.
   *
   * @param payloadJson The webhook payload as JSON string
   * @param signature The Base64-encoded signature from X-Payload-Signature header
   * @return WebhookPayload object if signature is valid
   * @throws TimelessPayException if verification or parsing fails
   */
  public WebhookPayload verifyAndParse(String payloadJson, String signature)
      throws TimelessPayException {
    if (!verifySignature(payloadJson, signature)) {
      throw new TimelessPayException("Invalid webhook signature");
    }
    return parsePayload(payloadJson);
  }
}
