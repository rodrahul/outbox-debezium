package org.acme.order.rest;

import java.time.LocalDateTime;
import java.util.List;

import org.acme.order.entity.PurchaseOrder;

import lombok.Builder;

/**
 * A value object that represents the response of an operation on a
 * {@link PurchaseOrder}
 */
@Builder
public record OrderOperationResponse(
    long id,
    long customerId,
    LocalDateTime orderDate,
    List<OrderLineDto> lineItems) {

  /**
   * Creates OrderOperationResponse from Purchase order
   * 
   * @param order
   * @return
   */
  public static OrderOperationResponse from(PurchaseOrder order) {
    List<OrderLineDto> lines = order.getLineItems()
        .stream()
        .map(l -> new OrderLineDto(l.getId(), l.getItem(), l.getQuantity(), l.getTotalPrice(), l.getStatus()))
        .toList();

    return OrderOperationResponse.builder()
        .id(order.getId())
        .customerId(order.getCustomerId())
        .orderDate(order.getOrderDate())
        .lineItems(lines)
        .build();
  }
}
