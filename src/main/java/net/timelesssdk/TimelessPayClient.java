package net.timelesssdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.UUID;
import net.timelesssdk.exception.SignatureException;
import net.timelesssdk.exception.TimelessPayApiException;
import net.timelesssdk.exception.TimelessPayException;
import net.timelesssdk.model.CheckoutSessionResponse;
import net.timelesssdk.model.CreateCheckoutSessionRequest;
import net.timelesssdk.model.ErrorResponse;
import net.timelesssdk.security.SignatureUtil;
import okhttp3.*;

/** Main client for interacting with the TimelessPay Payment Gateway Service. */
public class TimelessPayClient {
  private static final String CHECKOUT_SESSIONS_ENDPOINT = "/checkout-sessions/create";
  private static final String CONTENT_TYPE_JSON = "application/json";
  private static final String HEADER_CONTENT_TYPE = "Content-Type";
  private static final String HEADER_TIMELESS_ID = "X-Timeless-Id";
  private static final String HEADER_PAYLOAD_SIGNATURE = "X-Payload-Signature";
  private static final String HEADER_TENANT_ID = "X-Tenant-Id";
  private static final String HEADER_REQUEST_ID = "X-Request-Id";

  private final String baseUrl;
  private final String apiKeyId;
  private final String tenantId;
  private final PrivateKey privateKey;
  private final OkHttpClient httpClient;
  private final ObjectMapper objectMapper;

  /**
   * Creates a new TimelessPayClient.
   *
   * @param baseUrl The base URL of the PGS API (e.g., "http://api.timelesspay.net/api/v1/pgs")
   * @param apiKeyId Your API Key ID
   * @param tenantId Your tenant ID
   * @param privateKey Your private key (Base64-encoded PKCS#8 format)
   * @throws TimelessPayException if private key loading fails
   */
  public TimelessPayClient(String baseUrl, String apiKeyId, String tenantId, String privateKey)
      throws TimelessPayException {
    this(baseUrl, apiKeyId, tenantId, SignatureUtil.loadPrivateKey(privateKey));
  }

  /**
   * Creates a new TimelessPayClient.
   *
   * @param baseUrl The base URL of the PGS API
   * @param apiKeyId Your API Key ID
   * @param tenantId Your tenant ID
   * @param privateKey Your private key as PrivateKey object
   */
  public TimelessPayClient(
      String baseUrl, String apiKeyId, String tenantId, PrivateKey privateKey) {
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.apiKeyId = apiKeyId;
    this.tenantId = tenantId;
    this.privateKey = privateKey;
    this.httpClient = new OkHttpClient();
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  /**
   * Creates a checkout session.
   *
   * @param request The checkout session request
   * @return CheckoutSessionResponse
   * @throws TimelessPayException if the request fails
   */
  public CheckoutSessionResponse createCheckoutSession(CreateCheckoutSessionRequest request)
      throws TimelessPayException {
    try {
      // Serialize request to JSON
      String requestJson = objectMapper.writeValueAsString(request);

      // Generate signature
      String signature = SignatureUtil.sign(requestJson, privateKey);

      // Generate request ID if not provided
      String requestId = request.getCorrelationId();
      if (requestId == null || requestId.isEmpty()) {
        requestId = UUID.randomUUID().toString();
      }

      // Build request
      RequestBody body = RequestBody.create(requestJson, MediaType.get(CONTENT_TYPE_JSON));
      Request httpRequest =
          new Request.Builder()
              .url(baseUrl + CHECKOUT_SESSIONS_ENDPOINT)
              .post(body)
              .addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
              .addHeader(HEADER_TIMELESS_ID, apiKeyId)
              .addHeader(HEADER_PAYLOAD_SIGNATURE, signature)
              .addHeader(HEADER_TENANT_ID, tenantId)
              .addHeader(HEADER_REQUEST_ID, requestId)
              .build();

      // Execute request
      try (Response response = httpClient.newCall(httpRequest).execute()) {
        String responseBody = response.body() != null ? response.body().string() : "";

        if (response.isSuccessful()) {
          return objectMapper.readValue(responseBody, CheckoutSessionResponse.class);
        } else {
          // Try to parse error response
          ErrorResponse errorResponse = null;
          try {
            errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
          } catch (Exception e) {
            // Ignore parsing errors
          }

          String errorMessage =
              errorResponse != null && errorResponse.getMessage() != null
                  ? errorResponse.getMessage()
                  : "API request failed";
          String errorStatus = errorResponse != null ? errorResponse.getStatus() : null;

          throw new TimelessPayApiException(response.code(), errorMessage, errorStatus);
        }
      }
    } catch (IOException e) {
      throw new TimelessPayException("Failed to create checkout session", e);
    } catch (SignatureException e) {
      throw new TimelessPayException("Failed to sign request", e);
    }
  }

  /**
   * Gets the base URL.
   *
   * @return base URL
   */
  public String getBaseUrl() {
    return baseUrl;
  }

  /**
   * Gets the API Key ID.
   *
   * @return API Key ID
   */
  public String getApiKeyId() {
    return apiKeyId;
  }

  /**
   * Gets the tenant ID.
   *
   * @return tenant ID
   */
  public String getTenantId() {
    return tenantId;
  }

  /**
   * Constructs the embedded checkout page URL.
   *
   * @param nonce The nonce from the checkout session
   * @return The checkout page URL
   */
  public String getCheckoutUrl(String nonce) {
    return getCheckoutUrl(nonce, apiKeyId);
  }

  /**
   * Constructs the embedded checkout page URL from a checkout session response.
   *
   * @param response The checkout session response
   * @return The checkout page URL
   */
  public String getCheckoutUrl(CheckoutSessionResponse response) {
    return getCheckoutUrl(response.getNonce());
  }

  /**
   * Constructs the embedded checkout page URL.
   *
   * @param nonce The nonce from the checkout session
   * @param timelessId The Timeless ID (API Key ID)
   * @return The checkout page URL
   */
  public static String getCheckoutUrl(String nonce, String timelessId) {
    return String.format("https://embedded.timelesspay.net/checkout/%s/%s", nonce, timelessId);
  }
}
