package org.example;

public class PromotionResult {
    private final double discount;
    private final boolean freeDelivery;

    public PromotionResult(double discount, boolean freeDelivery) {
        this.discount = discount;
        this.freeDelivery = freeDelivery;
    }

    public double getDiscount() {
        return discount;
    }

    public boolean isFreeDelivery() {
        return freeDelivery;
    }
}
