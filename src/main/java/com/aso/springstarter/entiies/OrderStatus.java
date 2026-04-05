package com.aso.springstarter.entiies;

public enum OrderStatus {
    /**
    * When the customer creates an order
    */
    INITIATED,
    /**
    * When the customer added an item to the order
    */
    PENDING,
    /**
    * When the customer completed the order, no more items can be added.
    * Used as a history to view completed orders.
    */
    COMPLETED,
    /**
    * After a certain time, when the customer didn't add any items to the order,
    * the order will be expired. Can no longer be accessed or modified.
    */
    EXPIRED
}
