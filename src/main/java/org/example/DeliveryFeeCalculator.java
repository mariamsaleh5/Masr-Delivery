package org.example;

public class DeliveryFeeCalculator {
    private static final double BASE_FEE = 15.0;
    private static final double FREE_DISTANCE = 3.0;
    private static final double EXTRA_KM_FEE = 3.0;

    public double calculate(double distanceKm) {

        if (distanceKm <= FREE_DISTANCE) {
            return BASE_FEE;
        }

        double extraDistance = distanceKm - FREE_DISTANCE;

        return BASE_FEE + (extraDistance * EXTRA_KM_FEE);
    }

}
