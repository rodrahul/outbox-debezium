package org.acme.order.rest;

import org.acme.order.entity.OrderLineStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * A value object that represents updating a {@link OrderLine} status
 */
@Data
public class UpdateOrderLineRequest {

  @NotNull
  private OrderLineStatus newStatus;

}
