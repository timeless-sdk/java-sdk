# TimelessPay Integration Guide for Third-Party Systems

This guide explains how third-party systems integrate with the Payment Gateway Service (PGS) for checkout session creation and webhook notifications.

## Table of Contents

1. [Overview](#overview)
2. [Checkout Session Creation](#checkout-session-creation)
3. [Webhook Notifications](#webhook-notifications)
4. [Security](#security)

---

## Overview

The PGS integration consists of two main flows:

1. **Checkout Session Creation**: Your system creates a checkout session by calling PGS
2. **Webhook Notifications**: PGS sends webhook notifications to your system for various payment events

### Integration Flow

```
Your System                          PGS Service
     |                                    |
     |  1. Create Checkout Session        |
     |----------------------------------->|
     |     POST /checkout-sessions/create |
     |                                    |
     |  2. Checkout Session Response      |
     |<-----------------------------------|
     |                                    |
     |  3. Payment Processing (User)       |
     |                                    |
     |  4. Webhook Notification           |
     |<-----------------------------------|
     |     POST /api/webhooks/timeless     |
     |                                    |
```

---

## Checkout Session Creation

### Endpoint

**URL**: `POST {base-url}/checkout-sessions/create`

**Base URL**: Provided by PGS (e.g., `http://api.timelesspay.net/api/v1/pgs`)

### Request Headers

| Header | Required | Description |
|--------|----------|-------------|
| `Content-Type` | Yes | `application/json` |
| `X-Timeless-Id` | Yes | Your API Key ID provided by PGS |
| `X-Payload-Signature` | Yes | Base64-encoded ECDSA signature of the request body |
| `X-Tenant-Id` | Yes | Your tenant ID provided by PGS |
| `X-Request-Id` | No | Correlation ID for request tracking (auto-generated if not provided) |

### Request Body

```json
{
  "customerName": "John Doe",
  "customerPhoneNumber": "+251911234567",
  "nonce": "unique-nonce-value-12345",
  "items": [
    {
      "name": "Product 1",
      "description": "Product description",
      "price": 100.00,
      "quantity": 2,
      "total": 200.00
    }
  ],
  "sessionExpiresAt": "2024-01-01T12:00:00",
  "currency": "ETB",
  "returnUrl": "https://your-app.com/payment/return",
  "totalAmount": 200.00,
  "totalQuantity": 2,
  "correlationId": "optional-correlation-id"
}
```

### Request Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `customerName` | String | No | Customer's full name |
| `customerPhoneNumber` | String | Yes | Customer's phone number (10-15 characters) |
| `nonce` | String | Yes | Unique identifier for this request (must be unique per request) |
| `items` | Array | Yes | List of items (at least one item required) |
| `items[].name` | String | Yes | Item name |
| `items[].description` | String | No | Item description |
| `items[].price` | Number | Yes | Item price (must be >= 0) |
| `items[].quantity` | Integer | Yes | Item quantity (must be >= 1) |
| `items[].total` | Number | No | Item total (price × quantity) |
| `sessionExpiresAt` | DateTime | Yes | Session expiration time (ISO 8601 format, must be in the future) |
| `currency` | String | Yes | Currency code (e.g., "ETB", "USD") |
| `returnUrl` | String | No | URL to redirect after payment completion |
| `totalAmount` | Number | No | Total amount (calculated automatically if not provided) |
| `totalQuantity` | Integer | No | Total quantity (calculated automatically if not provided) |
| `correlationId` | String | No | Correlation ID for request tracking |

### Response (Success - 200 OK)

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "customerName": "John Doe",
  "customerPhoneNumber": "+251911234567",
  "nonce": "unique-nonce-value-12345",
  "apiKeyId": 123,
  "publicKey": "iY6D0AOgTGm9tpwxeXi561",
  "items": [
    {
      "name": "Product 1",
      "description": "Product description",
      "price": 100.00,
      "quantity": 2,
      "total": 200.00
    }
  ],
  "totalAmount": 200.00,
  "totalQuantity": 2,
  "sessionExpiresAt": "2024-01-01T12:00:00",
  "currency": "ETB",
  "returnUrl": "https://your-app.com/payment/return",
  "createdAt": "2024-01-01T11:00:00",
  "updatedAt": "2024-01-01T11:00:00"
}
```

### Response Fields

| Field | Type | Description |
|-------|------|-------------|
| `id` | String (UUID) | Checkout session ID |
| `customerName` | String | Customer's name |
| `customerPhoneNumber` | String | Customer's phone number |
| `nonce` | String | Nonce from request |
| `apiKeyId` | Long | API Key ID used for this session |
| `publicKey` | String | Public key for embedded checkout (if applicable) |
| `items` | Array | List of items |
| `totalAmount` | Number | Total amount |
| `totalQuantity` | Integer | Total quantity |
| `sessionExpiresAt` | DateTime | Session expiration time |
| `currency` | String | Currency code |
| `returnUrl` | String | Return URL |
| `createdAt` | DateTime | Session creation timestamp |
| `updatedAt` | DateTime | Session last update timestamp |

### Error Responses

#### 400 Bad Request
```json
{
  "message": "Validation error message",
  "status": "ERROR"
}
```

#### 401 Unauthorized
```json
{
  "message": "Invalid signature or API key",
  "status": "ERROR"
}
```

#### 500 Internal Server Error
```json
{
  "message": "Internal server error",
  "status": "ERROR"
}
```

### Signature Generation

The request body must be signed using **ECDSA with SHA384** algorithm:

1. Serialize the request body to JSON (without any signature field)
2. Sign the JSON string using your private key with ECDSA SHA384
3. Base64 encode the signature
4. Include the signature in the `X-Payload-Signature` header

**Algorithm**: `SHA384withECDSA`

**Example** (pseudo-code):
```java
String payloadJson = objectMapper.writeValueAsString(requestBody);
Signature signature = Signature.getInstance("SHA384withECDSA");
signature.initSign(privateKey);
signature.update(payloadJson.getBytes());
byte[] signatureBytes = signature.sign();
String base64Signature = Base64.getEncoder().encodeToString(signatureBytes);
```

---

## Webhook Notifications

PGS sends webhook notifications to your system when payment events occur. You must implement a webhook endpoint to receive and process these notifications.

### Webhook Endpoint

**URL**: `POST {your-webhook-url}/api/webhooks/timeless`

**Note**: The webhook URL must be configured in PGS for your API key. Contact PGS support to configure your webhook URL.

### Webhook Request Headers

| Header | Required | Description |
|--------|----------|-------------|
| `Content-Type` | Yes | `application/json` |
| `X-Payload-Signature` | Yes | Base64-encoded ECDSA signature of the webhook payload |

### Webhook Payload Structure

All webhook payloads follow this structure:

```json
{
  "event_type": "payment.success",
  "webhook_id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-01T12:00:00",
  "session_id": "550e8400-e29b-41d4-a716-446655440001",
  "payment_id": 12345,
  "status": "success",
  "amount": 200.00,
  "currency": "ETB",
  "customer_name": "John Doe",
  "customer_phone": "+251911234567",
  "items": [
    {
      "total_quantity": 2,
      "total_amount": 200.00
    }
  ],
  "metadata": {
    "transaction_id": 12345,
    "payment_method": "CBEBIRR",
    "total_amount": 200.00,
    "commission": 5.00,
    "business_id": "business-uuid",
    "created_at": "2024-01-01T11:00:00",
    "updated_at": "2024-01-01T12:00:00"
  }
}
```

### Webhook Payload Fields

| Field | Type | Description |
|-------|------|-------------|
| `event_type` | String | Event type (see event types below) |
| `webhook_id` | String (UUID) | Unique webhook identifier |
| `timestamp` | DateTime | Webhook timestamp (ISO 8601 format) |
| `session_id` | String (UUID) | Checkout session ID |
| `payment_id` | Long | Payment/transaction ID (null for session events) |
| `status` | String | Payment status (success, failed, pending, cancelled, refunded, created) |
| `amount` | Number | Payment amount |
| `currency` | String | Currency code |
| `customer_name` | String | Customer's name |
| `customer_phone` | String | Customer's phone number |
| `items` | Array | Items array (structure may vary) |
| `metadata` | Object | Additional metadata (structure varies by event type) |

### Webhook Event Types

#### 1. Payment Success

**Event Type**: `payment.success`

**Triggered When**: A payment transaction is completed successfully

**Payload Example**:
```json
{
  "event_type": "payment.success",
  "webhook_id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-01T12:00:00",
  "session_id": "550e8400-e29b-41d4-a716-446655440001",
  "payment_id": 12345,
  "status": "success",
  "amount": 200.00,
  "currency": "ETB",
  "customer_name": "John Doe",
  "customer_phone": "+251911234567",
  "items": [
    {
      "total_quantity": 2,
      "total_amount": 200.00
    }
  ],
  "metadata": {
    "transaction_id": 12345,
    "payment_method": "CBEBIRR",
    "total_amount": 200.00,
    "commission": 5.00,
    "business_id": "business-uuid",
    "created_at": "2024-01-01T11:00:00",
    "updated_at": "2024-01-01T12:00:00"
  }
}
```

#### 2. Payment Failed

**Event Type**: `payment.failed`

**Triggered When**: A payment transaction fails

**Payload Example**:
```json
{
  "event_type": "payment.failed",
  "webhook_id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-01T12:00:00",
  "session_id": "550e8400-e29b-41d4-a716-446655440001",
  "payment_id": 12345,
  "status": "failed",
  "amount": 200.00,
  "currency": "ETB",
  "customer_name": "John Doe",
  "customer_phone": "+251911234567",
  "items": [
    {
      "total_quantity": 2,
      "total_amount": 200.00
    }
  ],
  "metadata": {
    "transaction_id": 12345,
    "payment_method": "CBEBIRR",
    "total_amount": 200.00,
    "business_id": "business-uuid",
    "created_at": "2024-01-01T11:00:00",
    "updated_at": "2024-01-01T12:00:00"
  }
}
```

#### 3. Payment Pending

**Event Type**: `payment.pending`

**Triggered When**: A payment transaction is pending (awaiting confirmation)

**Payload Example**:
```json
{
  "event_type": "payment.pending",
  "webhook_id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-01T12:00:00",
  "session_id": "550e8400-e29b-41d4-a716-446655440001",
  "payment_id": 12345,
  "status": "pending",
  "amount": 200.00,
  "currency": "ETB",
  "customer_name": "John Doe",
  "customer_phone": "+251911234567",
  "items": [
    {
      "total_quantity": 2,
      "total_amount": 200.00
    }
  ],
  "metadata": {
    "transaction_id": 12345,
    "payment_method": "CBEBIRR",
    "total_amount": 200.00,
    "business_id": "business-uuid",
    "created_at": "2024-01-01T11:00:00",
    "updated_at": "2024-01-01T12:00:00"
  }
}
```

#### 4. Payment Cancelled

**Event Type**: `payment.cancelled`

**Triggered When**: A payment transaction is cancelled

**Payload Example**:
```json
{
  "event_type": "payment.cancelled",
  "webhook_id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-01T12:00:00",
  "session_id": "550e8400-e29b-41d4-a716-446655440001",
  "payment_id": 12345,
  "status": "cancelled",
  "amount": 200.00,
  "currency": "ETB",
  "customer_name": "John Doe",
  "customer_phone": "+251911234567",
  "items": [
    {
      "total_quantity": 2,
      "total_amount": 200.00
    }
  ],
  "metadata": {
    "transaction_id": 12345,
    "payment_method": "CBEBIRR",
    "total_amount": 200.00,
    "business_id": "business-uuid",
    "created_at": "2024-01-01T11:00:00",
    "updated_at": "2024-01-01T12:00:00"
  }
}
```

#### 5. Payment Refunded

**Event Type**: `payment.refunded`

**Triggered When**: A payment is refunded

**Payload Example**:
```json
{
  "event_type": "payment.refunded",
  "webhook_id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-01T12:00:00",
  "session_id": "550e8400-e29b-41d4-a716-446655440001",
  "payment_id": 12345,
  "status": "refunded",
  "amount": 200.00,
  "currency": "ETB",
  "customer_name": "John Doe",
  "customer_phone": "+251911234567",
  "items": [
    {
      "total_quantity": 2,
      "total_amount": 200.00
    }
  ],
  "metadata": {
    "transaction_id": 12345,
    "payment_method": "CBEBIRR",
    "total_amount": 200.00,
    "commission": 5.00,
    "business_id": "business-uuid",
    "created_at": "2024-01-01T11:00:00",
    "updated_at": "2024-01-01T12:00:00"
  }
}
```

#### 6. Session Created

**Event Type**: `session.created`

**Triggered When**: A checkout session is created

**Payload Example**:
```json
{
  "event_type": "session.created",
  "webhook_id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-01T11:00:00",
  "session_id": "550e8400-e29b-41d4-a716-446655440001",
  "payment_id": null,
  "status": "created",
  "amount": 200.00,
  "currency": "ETB",
  "customer_name": "John Doe",
  "customer_phone": "+251911234567",
  "items": [
    {
      "total_quantity": 2,
      "total_amount": 200.00
    }
  ],
  "metadata": {
    "session_id": "550e8400-e29b-41d4-a716-446655440001",
    "api_key_id": 123,
    "total_amount": 200.00,
    "total_quantity": 2,
    "session_expires_at": "2024-01-01T12:00:00",
    "created_at": "2024-01-01T11:00:00",
    "updated_at": "2024-01-01T11:00:00"
  }
}
```

### Webhook Response

Your webhook endpoint should return:

**Success (200 OK)**:
```
Webhook received and processed
```

**Error (401 Unauthorized)** - Invalid signature:
```
Invalid signature
```

**Error (500 Internal Server Error)** - Processing error:
```
Error processing webhook: <error message>
```

### Webhook Signature Verification

You must verify the webhook signature before processing:

1. Receive the webhook payload and `X-Payload-Signature` header
2. Serialize the payload to JSON (without any signature field)
3. Decode the signature from Base64
4. Verify the signature using the public key provided by PGS with ECDSA SHA384
5. Only process the webhook if signature verification succeeds

**Algorithm**: `SHA384withECDSA`

**Example** (pseudo-code):
```java
String payloadJson = objectMapper.writeValueAsString(payload);
byte[] signatureBytes = Base64.getDecoder().decode(signature);
Signature ecdsa = Signature.getInstance("SHA384withECDSA");
ecdsa.initVerify(publicKey);
ecdsa.update(payloadJson.getBytes());
boolean isValid = ecdsa.verify(signatureBytes);
```

### Webhook Best Practices

1. **Idempotency**: Webhooks may be sent multiple times. Use `webhook_id` to ensure idempotent processing.

2. **Async Processing**: Process webhooks asynchronously and return 200 OK immediately to avoid timeouts.

3. **Signature Verification**: Always verify webhook signatures before processing.

4. **Timeout**: Respond to webhooks within 30 seconds.

5. **HTTPS**: Use HTTPS for webhook URLs in production.

6. **Error Handling**: Implement retry logic for failed webhook processing.

7. **Logging**: Log all webhook events for audit and debugging.

---

## Security

### Authentication

- **API Key**: Use your `X-Timeless-Id` (API Key ID) in request headers
- **Signature**: All requests must be signed using ECDSA SHA384
- **Public Key**: Use the public key provided by PGS to verify webhook signatures

### Key Management

- **Private Key**: Used to sign checkout session creation requests (keep secure)
- **Public Key**: Used to verify webhook signatures (provided by PGS)
- **Key Pair**: Your private/public key pair is provided by PGS during onboarding

### Security Best Practices

1. **Never expose private keys** in client-side code or logs
2. **Always verify webhook signatures** before processing
3. **Use HTTPS** for all API communications in production
4. **Rotate keys** periodically as per security policy
5. **Monitor** for unauthorized access attempts

