package org.acme.shipment.messagelog;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConsumedMessageRepository implements PanacheRepositoryBase<ConsumedMessage, UUID> {

}
