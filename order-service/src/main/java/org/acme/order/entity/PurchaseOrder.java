package org.acme.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;

/**
 * An entity mapping that represents a purchase order.
 */
@Entity
public class PurchaseOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_order_ids")
  @SequenceGenerator(name = "purchase_order_ids", sequenceName = "seq_purchase_order", allocationSize = 50)
  private Long id;

  @NotNull
  private long customerId;

  @NotNull
  private LocalDateTime orderDate;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "purchaseOrder")
  private List<OrderLine> lineItems;

  public PurchaseOrder() {
    // NO arg constructor
  }

  /**
   * 
   * @param customerId
   * @param orderDate
   * @param lineItems
   */
  public PurchaseOrder(long customerId, LocalDateTime orderDate, List<OrderLine> lineItems) {
    this.customerId = customerId;
    this.orderDate = orderDate;
    this.lineItems = new ArrayList<>(lineItems);
    lineItems.forEach(line -> line.setPurchaseOrder(this));
  }

  public OrderLineStatus updateOrderLine(long orderLineId, OrderLineStatus newStatus) {
    for (OrderLine orderLine : lineItems) {
      if (orderLine.getId() == orderLineId) {
        OrderLineStatus oldStatus = orderLine.getStatus();
        orderLine.setStatus(newStatus);
        return oldStatus;
      }
    }

    throw new EntityNotFoundException("Order does not contain line with id " + orderLineId);
  }

  public BigDecimal getTotalValue() {
    return lineItems.stream()
        .map(OrderLine::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public long getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(long customerId) {
    this.customerId = customerId;
  }

  public LocalDateTime getOrderDate() {
    return this.orderDate;
  }

  public void setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
  }

  public List<OrderLine> getLineItems() {
    return this.lineItems;
  }

  public void setLineItems(List<OrderLine> lineItems) {
    this.lineItems = lineItems;
  }

  @Override
  public String toString() {
    return "PurchaseOrder [id=" + id + ", customerId=" + customerId + ", orderDate=" + orderDate + ", lineItems="
        + lineItems + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;

    if (obj == null)
      return false;

    if (getClass() != obj.getClass())
      return false;

    PurchaseOrder other = (PurchaseOrder) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;

    return true;
  }

}
