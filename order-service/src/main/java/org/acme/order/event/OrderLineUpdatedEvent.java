package org.acme.order.event;

import java.time.Instant;

import org.acme.order.entity.OrderLineStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.debezium.outbox.quarkus.ExportedEvent;

public class OrderLineUpdatedEvent implements ExportedEvent<String, JsonNode> {

  // Set the type enclosed inside the event
  private static final String TYPE = "Order";
  // Set the event type
  private static final String EVENT_TYPE = "OrderLineUpdated";

  public static final ObjectMapper mapper = new ObjectMapper();

  private final long orderId;
  private final long orderLineId;
  private final OrderLineStatus newStatus;
  private final OrderLineStatus oldStatus;
  private final Instant timestamp;

  /**
   * 
   * @param orderId
   * @param orderLineId
   * @param newStatus
   * @param oldStatus
   */
  public OrderLineUpdatedEvent(long orderId, long orderLineId, OrderLineStatus newStatus, OrderLineStatus oldStatus) {
    this.orderId = orderId;
    this.orderLineId = orderLineId;
    this.newStatus = newStatus;
    this.oldStatus = oldStatus;
    this.timestamp = Instant.now();
  }

  @Override
  public String getAggregateId() {
    return String.valueOf(orderId);
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
    return mapper.createObjectNode()
        .put("orderId", orderId)
        .put("orderLineId", orderLineId)
        .put("oldStatus", oldStatus.name())
        .put("newStatus", newStatus.name());
  }

}
