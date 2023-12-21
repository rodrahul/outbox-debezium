package org.acme.order;

import org.acme.order.entity.PurchaseOrder;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<PurchaseOrder> {

}
