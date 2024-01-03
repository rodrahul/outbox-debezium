package org.acme.shipment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShipmentRepository implements PanacheRepository<Shipment> {

}
