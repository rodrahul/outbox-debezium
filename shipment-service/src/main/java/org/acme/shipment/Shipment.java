package org.acme.shipment;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;

/**
 * An entity mapping that represents a Shipment
 */
@Entity
public class Shipment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "shipment_ids")
  @SequenceGenerator(name = "shipment_ids", sequenceName = "seq_shipments", allocationSize = 50)
  private Long id;

  @NotNull
  private Long customerId;

  // Should be unique, but not doing so for the sake of restarts during demo
  @NotNull
  private Long orderId;

  private LocalDateTime orderDate;

  public Shipment() {
  }

  /**
   * @param customerId
   * @param orderId
   * @param orderDate
   */
  public Shipment(@NotNull Long customerId, @NotNull Long orderId, LocalDateTime orderDate) {
    this.customerId = customerId;
    this.orderId = orderId;
    this.orderDate = orderDate;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public Long getOrderId() {
    return this.orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public LocalDateTime getOrderDate() {
    return this.orderDate;
  }

  public void setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
  }

  @Override
  public String toString() {
    return "Shipment [id=" + id + ", customerId=" + customerId + ", orderId=" + orderId + ", orderDate=" + orderDate
        + "]";
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
    Shipment other = (Shipment) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
