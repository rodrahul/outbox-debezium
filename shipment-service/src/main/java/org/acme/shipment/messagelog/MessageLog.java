package org.acme.shipment.messagelog;

import java.time.Instant;
import java.util.UUID;

import org.jboss.logging.Logger;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@ApplicationScoped
public class MessageLog {

  @Inject
  ConsumedMessageRepository consumedMessageRepo;

  @Inject
  Tracer tracer;

  @Inject
  Span span;

  @Inject
  Logger logger;

  @Transactional(value = TxType.MANDATORY)
  public void processed(UUID uuid) {
    span = tracer.spanBuilder("processed").startSpan();
    try (final Scope scope = span.makeCurrent()) {
      consumedMessageRepo.persist(new ConsumedMessage(uuid, Instant.now()));
    } finally {
      span.end();
    }
  }

  @Transactional(value = TxType.MANDATORY)
  public boolean alreadyProcessed(UUID eventId) {
    logger.debugf("Looking for event with id %s in message log", eventId);
    span = tracer.spanBuilder("alreadyProcessed").startSpan();
    try (final Scope scope = span.makeCurrent()) {
      return consumedMessageRepo.findByIdOptional(eventId).isPresent();
    } finally {
      span.end();
    }
  }

}
