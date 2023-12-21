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
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An entity mapping that represents a line item on a {@link PurchaseOrder}
 * entity.
 */
@Entity
@Data
@NoArgsConstructor
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

}
