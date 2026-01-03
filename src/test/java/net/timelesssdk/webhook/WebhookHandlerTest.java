package net.timelesssdk.webhook;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import net.timelesssdk.TestKeyPairGenerator;
import net.timelesssdk.exception.SignatureException;
import net.timelesssdk.exception.TimelessPayException;
import net.timelesssdk.security.SignatureUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebhookHandlerTest {
  private KeyPair keyPair;
  private PrivateKey privateKey;
  private PublicKey publicKey;
  private String publicKeyBase64;
  private WebhookHandler handler;
  private String validWebhookPayload;

  @BeforeEach
  void setUp() throws Exception {
    keyPair = TestKeyPairGenerator.generateKeyPair();
    privateKey = keyPair.getPrivate();
    publicKey = keyPair.getPublic();
    publicKeyBase64 = TestKeyPairGenerator.publicKeyToBase64(publicKey);
    handler = new WebhookHandler(publicKey);
    validWebhookPayload =
        "{\"event_type\":\"payment.success\",\"webhook_id\":\"550e8400-e29b-41d4-a716-446655440000\","
            + "\"timestamp\":\"2024-01-01T12:00:00\",\"session_id\":\"550e8400-e29b-41d4-a716-446655440001\","
            + "\"payment_id\":12345,\"status\":\"success\",\"amount\":200.00,\"currency\":\"ETB\","
            + "\"customer_name\":\"John Doe\",\"customer_phone\":\"+251911234567\"}";
  }

  @Test
  void testConstructorWithBase64Key() throws TimelessPayException {
    WebhookHandler handlerFromBase64 = new WebhookHandler(publicKeyBase64);
    assertNotNull(handlerFromBase64);
  }

  @Test
  void testConstructorWithPublicKey() {
    WebhookHandler handlerFromKey = new WebhookHandler(publicKey);
    assertNotNull(handlerFromKey);
  }

  @Test
  void testConstructorThrowsExceptionForInvalidKey() {
    assertThrows(TimelessPayException.class, () -> new WebhookHandler("invalid-key"));
  }

  @Test
  void testVerifySignatureWithValidSignature() throws SignatureException {
    String signature = SignatureUtil.sign(validWebhookPayload, privateKey);
    boolean isValid = handler.verifySignature(validWebhookPayload, signature);
    assertTrue(isValid);
  }

  @Test
  void testVerifySignatureWithInvalidSignature() throws SignatureException {
    String validSignature = SignatureUtil.sign(validWebhookPayload, privateKey);
    String modifiedPayload = validWebhookPayload.replace("success", "failed");
    boolean isValid = handler.verifySignature(modifiedPayload, validSignature);
    assertFalse(isValid);
  }

  @Test
  void testVerifySignatureThrowsExceptionForInvalidSignatureFormat() {
    assertThrows(
        SignatureException.class,
        () -> handler.verifySignature(validWebhookPayload, "invalid-signature"));
  }

  @Test
  void testParsePayload() throws TimelessPayException {
    WebhookPayload payload = handler.parsePayload(validWebhookPayload);
    assertNotNull(payload);
    assertEquals("payment.success", payload.getEventType());
    assertEquals(12345L, payload.getPaymentId());
    assertEquals("success", payload.getStatus());
  }

  @Test
  void testParsePayloadThrowsExceptionForInvalidJson() {
    String invalidJson = "{invalid json}";
    assertThrows(TimelessPayException.class, () -> handler.parsePayload(invalidJson));
  }

  @Test
  void testVerifyAndParseWithValidSignature() throws TimelessPayException, SignatureException {
    String signature = SignatureUtil.sign(validWebhookPayload, privateKey);
    WebhookPayload payload = handler.verifyAndParse(validWebhookPayload, signature);
    assertNotNull(payload);
    assertEquals("payment.success", payload.getEventType());
  }

  @Test
  void testVerifyAndParseThrowsExceptionForInvalidSignature() throws SignatureException {
    String signature = SignatureUtil.sign(validWebhookPayload, privateKey);
    String modifiedPayload = validWebhookPayload.replace("success", "failed");
    assertThrows(
        TimelessPayException.class, () -> handler.verifyAndParse(modifiedPayload, signature));
  }

  @Test
  void testVerifyAndParseThrowsExceptionForInvalidJson() throws SignatureException {
    String invalidJson = "{invalid json}";
    String signature = SignatureUtil.sign(invalidJson, privateKey);
    assertThrows(TimelessPayException.class, () -> handler.verifyAndParse(invalidJson, signature));
  }
}
