package org.acme.shipment;

import java.time.LocalDateTime;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@ApplicationScoped
public class ShipmentService {

  @Inject
  ShipmentRepository shipmentRepo;

  @Inject
  Tracer tracer;

  @Inject
  Span span;

  @Inject
  Logger logger;

  @Transactional(value = TxType.MANDATORY)
  public void orderCreated(JsonNode event) {
    logger.infof("Processing 'OrderCreated' event: %s", event);

    span = tracer.spanBuilder("orderCreated").startSpan();
    try (final Scope scope = span.makeCurrent()) {
      final long orderId = event.get("id").asLong();
      final long customerId = event.get("customerId").asLong();
      final LocalDateTime orderDate = LocalDateTime.parse(event.get("orderDate").asText());

      shipmentRepo.persist(new Shipment(customerId, orderId, orderDate));
    } finally {
      span.end();
    }
  }

  @Transactional(value = TxType.MANDATORY)
  public void orderLineUpdated(JsonNode event) {
    logger.infof("Processing 'OrderLineUpdated' event: %s", event);
    span = tracer.spanBuilder("orderLineUpdated").startSpan();
    try (final Scope scope = span.makeCurrent()) {
      span.addEvent("Processing 'OrderLineUpdated' event: " + event);
    } finally {
      span.end();
    }
  }

}
