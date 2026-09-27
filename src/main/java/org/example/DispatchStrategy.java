package org.example;

public interface DispatchStrategy {

    boolean canHandleOrder(Order order);

    double calculateDeliveryTime(double distanceKm);
}