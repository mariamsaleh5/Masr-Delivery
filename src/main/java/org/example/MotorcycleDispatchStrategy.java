package org.example;

public class MotorcycleDispatchStrategy
        implements DispatchStrategy {

    @Override
    public boolean canHandleOrder(Order order) {

        return true;
    }

    @Override
    public double calculateDeliveryTime(double distanceKm) {
        double speed = 50.0;
        return distanceKm / speed;
    }
}