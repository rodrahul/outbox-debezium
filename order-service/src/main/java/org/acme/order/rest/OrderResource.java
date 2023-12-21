package org.acme.order.rest;

import java.util.List;

import org.acme.order.OrderService;
import org.acme.order.entity.PurchaseOrder;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

  @Inject
  OrderService orderService;

  @Inject
  Logger logger;

  @GET
  public RestResponse<List<OrderOperationResponse>> listOrders() {
    var orders = orderService.listOrders();
    List<OrderOperationResponse> response = orders.stream()
        .map(OrderOperationResponse::from)
        .toList();
    return RestResponse.ok(response);
  }

  @GET
  @Path("/{id}")
  public RestResponse<OrderOperationResponse> getOrder(@RestPath Long id) {
    var order = orderService.findOrderById(id).orElse(null);
    if (order == null) {
      logger.debug("No Order found with id " + id);
      return RestResponse.noContent();
    }

    logger.debug("Found Order " + order);
    var response = OrderOperationResponse.from(order);
    return RestResponse.ok(response);
  }

  @POST
  public RestResponse<OrderOperationResponse> addOrder(@Valid CreateOrderRequest createOrderRequest) {
    PurchaseOrder order = createOrderRequest.toOrder();
    order = orderService.addOrder(order);
    OrderOperationResponse response = OrderOperationResponse.from(order);
    return RestResponse.ok(response);
  }

  @PUT
  @Path("/{orderId}/lines/{orderLineId}")
  public RestResponse<OrderOperationResponse> updateOrderLine(
      @RestPath Long orderId,
      @RestPath Long orderLineId,
      @Valid UpdateOrderLineRequest request

  ) {
    PurchaseOrder updatedOrder = orderService.updateOrderLine(orderId, orderLineId, request.getNewStatus());
    OrderOperationResponse response = OrderOperationResponse.from(updatedOrder);
    return RestResponse.ok(response);
  }

}
