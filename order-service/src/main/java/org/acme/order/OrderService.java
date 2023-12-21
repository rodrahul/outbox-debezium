package org.acme.order;

import java.util.List;
import java.util.Optional;

import org.acme.order.entity.OrderLineStatus;
import org.acme.order.entity.PurchaseOrder;
import org.acme.order.event.InvoiceCreatedEvent;
import org.acme.order.event.OrderCreatedEvent;
import org.acme.order.event.OrderLineUpdatedEvent;

import io.debezium.outbox.quarkus.ExportedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrderService {

  @Inject
  OrderRepository orderRepo;

  @Inject
  Event<ExportedEvent<?, ?>> event;

  public List<PurchaseOrder> listOrders() {
    return orderRepo.listAll();
  }

  /**
   * @param id
   * @return
   */
  public Optional<PurchaseOrder> findOrderById(Long id) {
    return orderRepo.findByIdOptional(id);
  }

  /**
   * Add a new {@link PurchaseOrder}.
   *
   * @param order the purchase order
   * @return the persisted purchase order
   */
  @Transactional
  public PurchaseOrder addOrder(PurchaseOrder order) {
    orderRepo.persist(order);

    // Fire events for newly created PurchaseOrder
    event.fire(new OrderCreatedEvent(order));
    event.fire(new InvoiceCreatedEvent(order));

    return order;
  }

  /**
   * Update the a {@link PurchaseOrder} line's status.
   *
   * @param orderId     the purchase order id
   * @param orderLineId the purchase order line id
   * @param newStatus   the new order line status
   * @return the updated purchase order
   */
  @Transactional
  public PurchaseOrder updateOrderLine(long orderId, long orderLineId, OrderLineStatus newStatus) {
    var order = orderRepo.findByIdOptional(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Order with id " + orderId + " could not be found"));

    OrderLineStatus oldStatus = order.updateOrderLine(orderLineId, newStatus);
    event.fire(new OrderLineUpdatedEvent(orderId, orderLineId, newStatus,
        oldStatus));

    return order;
  }

}
