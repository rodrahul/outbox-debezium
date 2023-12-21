package org.acme.order.rest;

import org.acme.order.entity.OrderLineStatus;

import jakarta.validation.constraints.NotNull;

/**
 * A value object that represents the response for a
 * {@link UpdateOrderLineRequest}.
 */
public record UpdateOrderLineResponse(
    @NotNull OrderLineStatus oldStatus,
    @NotNull OrderLineStatus newStatus

) {

}
