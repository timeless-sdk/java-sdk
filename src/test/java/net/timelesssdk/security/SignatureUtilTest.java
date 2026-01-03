package net.timelesssdk.security;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import net.timelesssdk.TestKeyPairGenerator;
import net.timelesssdk.exception.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SignatureUtilTest {
  private KeyPair keyPair;
  private PrivateKey privateKey;
  private PublicKey publicKey;
  private String privateKeyBase64;
  private String publicKeyBase64;
  private String testPayload;

  @BeforeEach
  void setUp() throws Exception {
    keyPair = TestKeyPairGenerator.generateKeyPair();
    privateKey = keyPair.getPrivate();
    publicKey = keyPair.getPublic();
    privateKeyBase64 = TestKeyPairGenerator.privateKeyToBase64(privateKey);
    publicKeyBase64 = TestKeyPairGenerator.publicKeyToBase64(publicKey);
    testPayload = "{\"test\":\"data\",\"value\":123}";
  }

  @Test
  void testSignWithPrivateKeyObject() throws SignatureException {
    String signature = SignatureUtil.sign(testPayload, privateKey);
    assertNotNull(signature);
    assertFalse(signature.isEmpty());
  }

  @Test
  void testSignWithBase64PrivateKey() throws SignatureException {
    String signature = SignatureUtil.sign(testPayload, privateKeyBase64);
    assertNotNull(signature);
    assertFalse(signature.isEmpty());
  }

  @Test
  void testSignThrowsExceptionForInvalidKey() {
    assertThrows(SignatureException.class, () -> SignatureUtil.sign(testPayload, "invalid-key"));
  }

  @Test
  void testVerifyWithPublicKeyObject() throws SignatureException {
    String signature = SignatureUtil.sign(testPayload, privateKey);
    boolean isValid = SignatureUtil.verify(testPayload, signature, publicKey);
    assertTrue(isValid);
  }

  @Test
  void testVerifyWithBase64PublicKey() throws SignatureException {
    String signature = SignatureUtil.sign(testPayload, privateKeyBase64);
    boolean isValid = SignatureUtil.verify(testPayload, signature, publicKeyBase64);
    assertTrue(isValid);
  }

  @Test
  void testVerifyFailsForModifiedPayload() throws SignatureException {
    String signature = SignatureUtil.sign(testPayload, privateKey);
    String modifiedPayload = "{\"test\":\"modified\"}";
    boolean isValid = SignatureUtil.verify(modifiedPayload, signature, publicKey);
    assertFalse(isValid);
  }

  @Test
  void testVerifyFailsForInvalidSignature() throws SignatureException {
    String validSignature = SignatureUtil.sign(testPayload, privateKey);
    String invalidSignature = "invalid-signature";
    assertThrows(
        SignatureException.class,
        () -> SignatureUtil.verify(testPayload, invalidSignature, publicKey));
  }

  @Test
  void testVerifyThrowsExceptionForInvalidPublicKey() throws SignatureException {
    String signature = SignatureUtil.sign(testPayload, privateKey);
    assertThrows(
        SignatureException.class,
        () -> SignatureUtil.verify(testPayload, signature, "invalid-public-key"));
  }

  @Test
  void testLoadPrivateKey() throws SignatureException {
    PrivateKey loadedKey = SignatureUtil.loadPrivateKey(privateKeyBase64);
    assertNotNull(loadedKey);
    assertEquals(privateKey, loadedKey);
  }

  @Test
  void testLoadPrivateKeyThrowsExceptionForInvalidKey() {
    assertThrows(SignatureException.class, () -> SignatureUtil.loadPrivateKey("invalid-key"));
  }

  @Test
  void testLoadPublicKey() throws SignatureException {
    PublicKey loadedKey = SignatureUtil.loadPublicKey(publicKeyBase64);
    assertNotNull(loadedKey);
    assertEquals(publicKey, loadedKey);
  }

  @Test
  void testLoadPublicKeyThrowsExceptionForInvalidKey() {
    assertThrows(SignatureException.class, () -> SignatureUtil.loadPublicKey("invalid-key"));
  }

  @Test
  void testSignAndVerifyRoundTrip() throws SignatureException {
    String signature = SignatureUtil.sign(testPayload, privateKey);
    boolean isValid = SignatureUtil.verify(testPayload, signature, publicKey);
    assertTrue(isValid);
  }

  @Test
  void testSignAndVerifyWithBase64Keys() throws SignatureException {
    String signature = SignatureUtil.sign(testPayload, privateKeyBase64);
    boolean isValid = SignatureUtil.verify(testPayload, signature, publicKeyBase64);
    assertTrue(isValid);
  }

  @Test
  void testSignWithEmptyPayload() throws SignatureException {
    String emptyPayload = "";
    String signature = SignatureUtil.sign(emptyPayload, privateKey);
    assertNotNull(signature);
    boolean isValid = SignatureUtil.verify(emptyPayload, signature, publicKey);
    assertTrue(isValid);
  }

  @Test
  void testSignWithLargePayload() throws SignatureException {
    StringBuilder largePayload = new StringBuilder();
    for (int i = 0; i < 1000; i++) {
      largePayload.append("{\"key").append(i).append("\":\"value").append(i).append("\"},");
    }
    String payload = largePayload.toString();
    String signature = SignatureUtil.sign(payload, privateKey);
    boolean isValid = SignatureUtil.verify(payload, signature, publicKey);
    assertTrue(isValid);
  }
}
