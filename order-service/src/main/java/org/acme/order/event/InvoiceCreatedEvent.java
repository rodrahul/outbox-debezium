package org.acme.order.event;

import java.time.Instant;

import org.acme.order.entity.PurchaseOrder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.debezium.outbox.quarkus.ExportedEvent;

/**
 * A 'Customer' event that indicates an invoice has been created.
 */
public class InvoiceCreatedEvent implements ExportedEvent<String, JsonNode> {

  // Set the type enclosed inside the event
  private static final String TYPE = "Customer";
  // Set the event type
  private static final String EVENT_TYPE = "InvoiceCreated";

  public static final ObjectMapper mapper = new ObjectMapper();

  private final long customerId;
  private final JsonNode order;
  private final Instant timestamp;

  /**
   * Create InvoiceCreatedEvent from PurchaseOrder
   * 
   * @param order
   */
  public InvoiceCreatedEvent(PurchaseOrder order) {
    this.customerId = order.getCustomerId();
    this.timestamp = Instant.now();
    this.order = convertToJson(order);
  }

  private JsonNode convertToJson(PurchaseOrder order) {
    ObjectNode asJson = mapper.createObjectNode()
        .put("orderId", order.getId())
        .put("invoiceDate", order.getOrderDate().toString())
        .put("invoiceValue", order.getTotalValue());
    return asJson;
  }

  @Override
  public String getAggregateId() {
    return String.valueOf(customerId);
  }

  @Override
  public String getAggregateType() {
    return TYPE;
  }

  @Override
  public String getType() {
    return EVENT_TYPE;
  }

  @Override
  public Instant getTimestamp() {
    return timestamp;
  }

  @Override
  public JsonNode getPayload() {
    return order;
  }

}
