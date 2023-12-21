package org.acme.order.event;

import java.time.Instant;
import java.util.List;

import org.acme.order.entity.PurchaseOrder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.debezium.outbox.quarkus.ExportedEvent;

/**
 * An 'Order' event that indicates an order has been created
 */
public class OrderCreatedEvent implements ExportedEvent<String, JsonNode> {

  // Set the type enclosed inside the event
  private static final String TYPE = "Order";
  // Set the event type
  private static final String EVENT_TYPE = "OrderCreated";

  public static final ObjectMapper mapper = new ObjectMapper();

  private final long id;
  private final JsonNode order;
  private final Instant timestamp;

  /**
   * Create OrderCreatedEvent from PurchaseOrder
   * 
   * @param order
   */
  public OrderCreatedEvent(PurchaseOrder order) {
    this.id = order.getId();
    this.timestamp = Instant.now();
    this.order = convertToJson(order);
  }

  private JsonNode convertToJson(PurchaseOrder order) {
    ObjectNode asJson = mapper.createObjectNode()
        .put("id", order.getId())
        .put("customerId", order.getCustomerId())
        .put("orderDate", order.getOrderDate().toString());

    ArrayNode items = asJson.putArray("lineItems");

    List<ObjectNode> linesAsJson = order.getLineItems().stream()
        .map(orderLine -> mapper.createObjectNode()
            .put("id", orderLine.getId())
            .put("item", orderLine.getItem())
            .put("quantity", orderLine.getQuantity())
            .put("totalPrice", orderLine.getTotalPrice())
            .put("status", orderLine.getStatus().name()))
        .toList();
    items.addAll(linesAsJson);
    return asJson;
  }

  @Override
  public String getAggregateId() {
    return String.valueOf(id);
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
