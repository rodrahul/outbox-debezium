package org.acme.order.rest;

import java.math.BigDecimal;

import org.acme.order.entity.OrderLine;
import org.acme.order.entity.OrderLineStatus;
import org.acme.order.entity.PurchaseOrder;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A value object that represents an {@link OrderLine} for a
 * {@link PurchaseOrder}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineDto {

  private Long id;
  
  @NotNull
  private String item;
  
  @NotNull
  private Integer quantity;
  
  @NotNull
  private BigDecimal totalPrice;
  
  @NotNull
  private OrderLineStatus status;

  public OrderLineDto(String item, Integer quantity, BigDecimal totalPrice) {
    this.item = item;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.status = OrderLineStatus.ENTERED;
  }

}
