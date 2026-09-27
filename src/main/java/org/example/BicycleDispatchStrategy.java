package org.example;

public class BicycleDispatchStrategy
        implements DispatchStrategy {

    @Override
    public boolean canHandleOrder(Order order) {
        // bicycle rules
        return true;
    }

    @Override
    public double calculateDeliveryTime(double distanceKm) {
        double speed = 20.0;
        return distanceKm / speed;
    }
}