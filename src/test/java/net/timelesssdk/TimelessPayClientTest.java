package net.timelesssdk;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import net.timelesssdk.exception.TimelessPayApiException;
import net.timelesssdk.exception.TimelessPayException;
import net.timelesssdk.model.CheckoutItem;
import net.timelesssdk.model.CheckoutSessionResponse;
import net.timelesssdk.model.CreateCheckoutSessionRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimelessPayClientTest {
  private MockWebServer mockWebServer;
  private TimelessPayClient client;
  private KeyPair keyPair;
  private PrivateKey privateKey;
  private String baseUrl;
  private String apiKeyId;
  private String tenantId;
  private CreateCheckoutSessionRequest request;

  @BeforeEach
  void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    baseUrl = mockWebServer.url("/").toString().replaceAll("/$", "");
    apiKeyId = "123";
    tenantId = "tenant-123";
    keyPair = TestKeyPairGenerator.generateKeyPair();
    privateKey = keyPair.getPrivate();
    client = new TimelessPayClient(baseUrl, apiKeyId, tenantId, privateKey);

    // Create test request
    request = new CreateCheckoutSessionRequest();
    request.setCustomerName("John Doe");
    request.setCustomerPhoneNumber("+251911234567");
    request.setNonce("test-nonce-123");
    request.setCurrency("ETB");
    request.setSessionExpiresAt(LocalDateTime.now().plusHours(1));

    CheckoutItem item = new CheckoutItem();
    item.setName("Product 1");
    item.setDescription("Test product");
    item.setPrice(new BigDecimal("100.00"));
    item.setQuantity(2);
    item.setTotal(new BigDecimal("200.00"));

    List<CheckoutItem> items = new ArrayList<>();
    items.add(item);
    request.setItems(items);
    request.setTotalAmount(new BigDecimal("200.00"));
    request.setTotalQuantity(2);
  }

  @AfterEach
  void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void testConstructorWithBase64Key() throws TimelessPayException {
    String privateKeyBase64 = TestKeyPairGenerator.privateKeyToBase64(privateKey);
    TimelessPayClient clientFromBase64 =
        new TimelessPayClient(baseUrl, apiKeyId, tenantId, privateKeyBase64);
    assertNotNull(clientFromBase64);
    assertEquals(baseUrl, clientFromBase64.getBaseUrl());
    assertEquals(apiKeyId, clientFromBase64.getApiKeyId());
    assertEquals(tenantId, clientFromBase64.getTenantId());
  }

  @Test
  void testConstructorWithPrivateKey() {
    TimelessPayClient clientFromKey =
        new TimelessPayClient(baseUrl, apiKeyId, tenantId, privateKey);
    assertNotNull(clientFromKey);
    assertEquals(baseUrl, clientFromKey.getBaseUrl());
  }

  @Test
  void testConstructorRemovesTrailingSlash() {
    String urlWithSlash = baseUrl + "/";
    TimelessPayClient clientWithSlash =
        new TimelessPayClient(urlWithSlash, apiKeyId, tenantId, privateKey);
    assertEquals(baseUrl, clientWithSlash.getBaseUrl());
  }

  @Test
  void testConstructorThrowsExceptionForInvalidPrivateKey() {
    assertThrows(
        TimelessPayException.class,
        () -> new TimelessPayClient(baseUrl, apiKeyId, tenantId, "invalid-key"));
  }

  @Test
  void testCreateCheckoutSessionSuccess() throws Exception {
    CheckoutSessionResponse mockResponse = new CheckoutSessionResponse();
    mockResponse.setId("session-id-123");
    mockResponse.setNonce("test-nonce-123");
    mockResponse.setApiKeyId(123L);
    mockResponse.setPublicKey("public-key");
    mockResponse.setTotalAmount(new BigDecimal("200.00"));
    mockResponse.setCurrency("ETB");

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String responseJson = mapper.writeValueAsString(mockResponse);

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(responseJson));

    CheckoutSessionResponse response = client.createCheckoutSession(request);

    assertNotNull(response);
    assertEquals("session-id-123", response.getId());
    assertEquals("test-nonce-123", response.getNonce());
    assertEquals(123L, response.getApiKeyId());
  }

  @Test
  void testCreateCheckoutSessionWith400Error() throws Exception {
    String errorJson = "{\"message\":\"Validation error\",\"status\":\"ERROR\"}";
    mockWebServer.enqueue(new MockResponse().setResponseCode(400).setBody(errorJson));

    TimelessPayApiException exception =
        assertThrows(TimelessPayApiException.class, () -> client.createCheckoutSession(request));
    assertEquals(400, exception.getStatusCode());
    assertEquals("Validation error", exception.getErrorMessage());
  }

  @Test
  void testCreateCheckoutSessionWith401Error() throws Exception {
    String errorJson = "{\"message\":\"Invalid signature\",\"status\":\"ERROR\"}";
    mockWebServer.enqueue(new MockResponse().setResponseCode(401).setBody(errorJson));

    TimelessPayApiException exception =
        assertThrows(TimelessPayApiException.class, () -> client.createCheckoutSession(request));
    assertEquals(401, exception.getStatusCode());
    assertEquals("Invalid signature", exception.getErrorMessage());
  }

  @Test
  void testCreateCheckoutSessionWith500Error() throws Exception {
    String errorJson = "{\"message\":\"Internal server error\",\"status\":\"ERROR\"}";
    mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody(errorJson));

    TimelessPayApiException exception =
        assertThrows(TimelessPayApiException.class, () -> client.createCheckoutSession(request));
    assertEquals(500, exception.getStatusCode());
  }

  @Test
  void testCreateCheckoutSessionWithInvalidResponse() throws Exception {
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("invalid json"));

    assertThrows(TimelessPayException.class, () -> client.createCheckoutSession(request));
  }

  @Test
  void testCreateCheckoutSessionGeneratesRequestId() throws Exception {
    request.setCorrelationId(null);

    CheckoutSessionResponse mockResponse = new CheckoutSessionResponse();
    mockResponse.setId("session-id-123");
    mockResponse.setNonce("test-nonce-123");

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String responseJson = mapper.writeValueAsString(mockResponse);

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(responseJson));

    client.createCheckoutSession(request);

    // Verify request was made (request ID should be generated)
    assertNotNull(mockWebServer.takeRequest());
  }

  @Test
  void testCreateCheckoutSessionUsesProvidedCorrelationId() throws Exception {
    String correlationId = "custom-correlation-id";
    request.setCorrelationId(correlationId);

    CheckoutSessionResponse mockResponse = new CheckoutSessionResponse();
    mockResponse.setId("session-id-123");
    mockResponse.setNonce("test-nonce-123");

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String responseJson = mapper.writeValueAsString(mockResponse);

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(responseJson));

    client.createCheckoutSession(request);

    var recordedRequest = mockWebServer.takeRequest();
    assertEquals(correlationId, recordedRequest.getHeader("X-Request-Id"));
  }

  @Test
  void testGetCheckoutUrlWithNonce() {
    String nonce = "test-nonce-123";
    String url = client.getCheckoutUrl(nonce);
    String expected =
        String.format("https://embedded.timelesspay.net/checkout/%s/%s", nonce, apiKeyId);
    assertEquals(expected, url);
  }

  @Test
  void testGetCheckoutUrlWithResponse() throws Exception {
    CheckoutSessionResponse response = new CheckoutSessionResponse();
    response.setNonce("test-nonce-123");
    response.setApiKeyId(123L);

    CheckoutSessionResponse mockResponse = new CheckoutSessionResponse();
    mockResponse.setId("session-id-123");
    mockResponse.setNonce("test-nonce-123");

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String responseJson = mapper.writeValueAsString(mockResponse);

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(responseJson));

    CheckoutSessionResponse createdResponse = client.createCheckoutSession(request);
    String url = client.getCheckoutUrl(createdResponse);
    String expected =
        String.format(
            "https://embedded.timelesspay.net/checkout/%s/%s", "test-nonce-123", apiKeyId);
    assertEquals(expected, url);
  }

  @Test
  void testGetCheckoutUrlStatic() {
    String nonce = "test-nonce-123";
    String timelessId = "123";
    String url = TimelessPayClient.getCheckoutUrl(nonce, timelessId);
    String expected = "https://embedded.timelesspay.net/checkout/test-nonce-123/123";
    assertEquals(expected, url);
  }

  @Test
  void testGetters() {
    assertEquals(baseUrl, client.getBaseUrl());
    assertEquals(apiKeyId, client.getApiKeyId());
    assertEquals(tenantId, client.getTenantId());
  }
}
