package org.acme.order.rest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.acme.order.entity.OrderLine;
import org.acme.order.entity.PurchaseOrder;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * A value object that represents a request to create a {@link PurchaseOrder}.
 */
@Data
public class CreateOrderRequest {
  
  @NotNull
  private Long customerId;

  @NotNull
  private List<OrderLineDto> lineItems;

  public CreateOrderRequest() {
    this.lineItems = new ArrayList<>();
  }

  /**
   * Creates a PurchaseOrder from CreateOrderRequest
   * 
   * @return
   */
  public PurchaseOrder toOrder() {
    var lines = lineItems.stream()
        .map(l -> new OrderLine(l.getItem(), l.getQuantity(), l.getTotalPrice()))
        .toList();

    return new PurchaseOrder(customerId, LocalDateTime.now(), lines);
  }

}
