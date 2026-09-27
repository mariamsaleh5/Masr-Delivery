package org.example;

public class ServiceFeeCalculator {

    private static final double SERVICE_RATE = 0.10;

    public double calculate(double subtotal) {
        return Math.round(subtotal * SERVICE_RATE * 100.0) / 100.0;
    }
}
