package org.acme.shipment.facade;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import org.apache.kafka.common.header.Header;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class KafkaEventConsumer {

  @Inject
  Logger logger;

  @Inject
  OrderEventHandler orderEventHandler;

  @Inject
  Tracer tracer;

  @Inject
  Span span;

  @Incoming("orders")
  @RunOnVirtualThread
  public CompletionStage<Void> onMessage(KafkaRecord<String, String> message) {
    span = tracer.spanBuilder("onMessage").startSpan();
    try (final Scope scope = span.makeCurrent()) {
      logger.infof("Kafka message with key = %s, value = %s arrived", message.getKey(), message.getPayload());
      span.addEvent("Kafka message with key = " + message.getKey() + " arrived");

      String eventId = getHeaderAsString(message, "id");
      String eventType = getHeaderAsString(message, "eventType");

      orderEventHandler.onOrderEvent(
          UUID.fromString(eventId),
          eventType,
          message.getKey(),
          message.getPayload(),
          message.getTimestamp());

      message.ack();
      span.addEvent("ack");
    } catch (Exception e) {
      logger.error("Error while preparing shipment");
      span.addEvent("Error while preparing shipment");
      throw e;
    } finally {
      span.end();
    }

    return message.ack();
  }

  private String getHeaderAsString(KafkaRecord<?, ?> kafkaRecord, String name) {
    Header header = kafkaRecord.getHeaders().lastHeader(name);
    if (header == null) {
      throw new IllegalArgumentException("Expected record header '" + name + "' not present");
    }

    return new String(header.value(), StandardCharsets.UTF_8);
  }

}
