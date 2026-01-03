package net.timelesssdk;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

/** Utility class for generating test key pairs. */
public class TestKeyPairGenerator {
  private static final String ALGORITHM = "EC";
  private static final String CURVE_NAME = "secp384r1";

  /**
   * Generates a test key pair for ECDSA.
   *
   * @return KeyPair for testing
   * @throws Exception if key generation fails
   */
  public static KeyPair generateKeyPair() throws Exception {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
    ECGenParameterSpec ecSpec = new ECGenParameterSpec(CURVE_NAME);
    keyGen.initialize(ecSpec);
    return keyGen.generateKeyPair();
  }

  /**
   * Converts a private key to Base64-encoded PKCS#8 format.
   *
   * @param privateKey The private key
   * @return Base64-encoded private key
   */
  public static String privateKeyToBase64(PrivateKey privateKey) {
    return Base64.getEncoder().encodeToString(privateKey.getEncoded());
  }

  /**
   * Converts a public key to Base64-encoded X.509 format.
   *
   * @param publicKey The public key
   * @return Base64-encoded public key
   */
  public static String publicKeyToBase64(PublicKey publicKey) {
    return Base64.getEncoder().encodeToString(publicKey.getEncoded());
  }
}
