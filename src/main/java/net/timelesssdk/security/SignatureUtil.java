package net.timelesssdk.security;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import net.timelesssdk.exception.SignatureException;

/** Utility class for ECDSA SHA384 signature generation and verification. */
public class SignatureUtil {
  private static final String ALGORITHM = "SHA384withECDSA";
  private static final String KEY_ALGORITHM = "EC";

  /**
   * Signs a payload using ECDSA SHA384 with a private key.
   *
   * @param payload The payload to sign (JSON string)
   * @param privateKey The private key in PKCS#8 format (Base64 encoded)
   * @return Base64-encoded signature
   * @throws SignatureException if signing fails
   */
  public static String sign(String payload, String privateKey) throws SignatureException {
    try {
      byte[] keyBytes = Base64.getDecoder().decode(privateKey);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      PrivateKey privKey = keyFactory.generatePrivate(keySpec);

      Signature signature = Signature.getInstance(ALGORITHM);
      signature.initSign(privKey);
      signature.update(payload.getBytes(StandardCharsets.UTF_8));
      byte[] signatureBytes = signature.sign();

      return Base64.getEncoder().encodeToString(signatureBytes);
    } catch (Exception e) {
      throw new SignatureException("Failed to sign payload", e);
    }
  }

  /**
   * Signs a payload using ECDSA SHA384 with a PrivateKey object.
   *
   * @param payload The payload to sign (JSON string)
   * @param privateKey The private key
   * @return Base64-encoded signature
   * @throws SignatureException if signing fails
   */
  public static String sign(String payload, PrivateKey privateKey) throws SignatureException {
    try {
      Signature signature = Signature.getInstance(ALGORITHM);
      signature.initSign(privateKey);
      signature.update(payload.getBytes(StandardCharsets.UTF_8));
      byte[] signatureBytes = signature.sign();

      return Base64.getEncoder().encodeToString(signatureBytes);
    } catch (Exception e) {
      throw new SignatureException("Failed to sign payload", e);
    }
  }

  /**
   * Verifies a signature using ECDSA SHA384 with a public key.
   *
   * @param payload The payload that was signed (JSON string)
   * @param signature The Base64-encoded signature
   * @param publicKey The public key in X.509 format (Base64 encoded)
   * @return true if signature is valid, false otherwise
   * @throws SignatureException if verification fails
   */
  public static boolean verify(String payload, String signature, String publicKey)
      throws SignatureException {
    try {
      byte[] keyBytes = Base64.getDecoder().decode(publicKey);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      PublicKey pubKey = keyFactory.generatePublic(keySpec);

      byte[] signatureBytes = Base64.getDecoder().decode(signature);

      Signature ecdsa = Signature.getInstance(ALGORITHM);
      ecdsa.initVerify(pubKey);
      ecdsa.update(payload.getBytes(StandardCharsets.UTF_8));

      return ecdsa.verify(signatureBytes);
    } catch (Exception e) {
      throw new SignatureException("Failed to verify signature", e);
    }
  }

  /**
   * Verifies a signature using ECDSA SHA384 with a PublicKey object.
   *
   * @param payload The payload that was signed (JSON string)
   * @param signature The Base64-encoded signature
   * @param publicKey The public key
   * @return true if signature is valid, false otherwise
   * @throws SignatureException if verification fails
   */
  public static boolean verify(String payload, String signature, PublicKey publicKey)
      throws SignatureException {
    try {
      byte[] signatureBytes = Base64.getDecoder().decode(signature);

      Signature ecdsa = Signature.getInstance(ALGORITHM);
      ecdsa.initVerify(publicKey);
      ecdsa.update(payload.getBytes(StandardCharsets.UTF_8));

      return ecdsa.verify(signatureBytes);
    } catch (Exception e) {
      throw new SignatureException("Failed to verify signature", e);
    }
  }

  /**
   * Loads a private key from a Base64-encoded PKCS#8 string.
   *
   * @param privateKeyBase64 Base64-encoded private key
   * @return PrivateKey object
   * @throws SignatureException if key loading fails
   */
  public static PrivateKey loadPrivateKey(String privateKeyBase64) throws SignatureException {
    try {
      byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      return keyFactory.generatePrivate(keySpec);
    } catch (Exception e) {
      throw new SignatureException("Failed to load private key", e);
    }
  }

  /**
   * Loads a public key from a Base64-encoded X.509 string.
   *
   * @param publicKeyBase64 Base64-encoded public key
   * @return PublicKey object
   * @throws SignatureException if key loading fails
   */
  public static PublicKey loadPublicKey(String publicKeyBase64) throws SignatureException {
    try {
      byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      return keyFactory.generatePublic(keySpec);
    } catch (Exception e) {
      throw new SignatureException("Failed to load public key", e);
    }
  }
}
