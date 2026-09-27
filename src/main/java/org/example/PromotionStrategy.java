package org.example;

public interface PromotionStrategy {
    public PromotionResult apply(
            double subtotal,
            double deliveryFee
    );
}
