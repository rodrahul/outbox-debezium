package org.acme.order.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;

/**
 * An entity mapping that represents a line item on a {@link PurchaseOrder}
 * entity.
 */
@Entity
public class OrderLine {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_line_ids")
  @SequenceGenerator(name = "order_line_ids", sequenceName = "seq_order_line", allocationSize = 50)
  private Long id;

  @NotNull
  private String item;

  @NotNull
  private int quantity;

  @NotNull
  private BigDecimal totalPrice;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private PurchaseOrder purchaseOrder;

  @Enumerated(EnumType.STRING)
  private OrderLineStatus status;

  public OrderLine() {
    // No arg constructor
  }

  /**
   * @param item
   * @param quantity
   * @param totalPrice
   */
  public OrderLine(String item, int quantity, BigDecimal totalPrice) {
    this.item = item;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.status = OrderLineStatus.ENTERED;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getItem() {
    return this.item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getTotalPrice() {
    return this.totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public PurchaseOrder getPurchaseOrder() {
    return this.purchaseOrder;
  }

  public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
    this.purchaseOrder = purchaseOrder;
  }

  public OrderLineStatus getStatus() {
    return this.status;
  }

  public void setStatus(OrderLineStatus status) {
    this.status = status;
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
    OrderLine other = (OrderLine) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;

    return true;
  }

  @Override
  public String toString() {
    return "OrderLine [id=" + id + ", item=" + item + ", quantity=" + quantity + ", totalPrice=" + totalPrice
        + ", purchaseOrderId=" + purchaseOrder.getId() + ", status=" + status + "]";
  }

}
