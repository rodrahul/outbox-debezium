package org.acme.order.entity;

/**
 * Various statuses in which a {@link OrderLine} may be within.
 */
public enum OrderLineStatus {
  ENTERED,
  CANCELLED,
  SHIPPED
}