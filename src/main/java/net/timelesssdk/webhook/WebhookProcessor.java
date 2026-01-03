package net.timelesssdk.webhook;

/**
 * Interface for processing webhook events. Implement this interface to handle different webhook
 * event types.
 */
public interface WebhookProcessor {
  /**
   * Processes a webhook payload.
   *
   * @param payload The webhook payload
   * @throws Exception if processing fails
   */
  void process(WebhookPayload payload) throws Exception;
}
