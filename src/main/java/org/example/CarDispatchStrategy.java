package org.example;

public class CarDispatchStrategy
        implements DispatchStrategy {

    @Override
    public boolean canHandleOrder(Order order) {
        // car rules
        return true;
    }

    @Override
    public double calculateDeliveryTime(double distanceKm) {
        double speed = 60.0;
        return distanceKm / speed;
    }
}