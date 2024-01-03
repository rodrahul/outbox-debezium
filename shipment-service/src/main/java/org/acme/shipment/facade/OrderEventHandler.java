package org.acme.shipment.facade;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import org.acme.shipment.ShipmentService;
import org.acme.shipment.messagelog.MessageLog;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrderEventHandler {

  @Inject
  MessageLog msgLog;

  @Inject
  ShipmentService shipmentService;

  @Inject
  Logger logger;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Transactional
  public void onOrderEvent(UUID eventId, String eventType, String key, String event, Instant ts) {
    if (msgLog.alreadyProcessed(eventId)) {
      logger.infof("Event with UUID %s was already retrieved, ignoring it", eventId);
      return;
    }

    JsonNode eventPayload = deserialize(event);

    logger.infof("Received 'Order' event -- key: %s, event id: '%s', event type: '%s', ts: '%s'", key, eventId,
        eventType, ts);

    if (eventType.equals("OrderCreated")) {
      shipmentService.orderCreated(eventPayload);
    } else if (eventType.equals("OrderLineUpdated")) {
      shipmentService.orderLineUpdated(eventPayload);
    } else {
      logger.warn("Unkown event type");
    }

    msgLog.processed(eventId);
  }

  private JsonNode deserialize(String event) {
    JsonNode eventPayload;

    try {
      eventPayload = objectMapper.readTree(event);
    } catch (IOException e) {
      throw new RuntimeException("Couldn't deserialize event", e); // NOSONAR
    }

    return eventPayload.has("schema") ? eventPayload.get("payload") : eventPayload;
  }

}
