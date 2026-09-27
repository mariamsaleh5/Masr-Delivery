package org.example;

public class FreeDeliveryPromotion implements PromotionStrategy{
    @Override
    public PromotionResult apply(double subtotal, double deliveryFee) {
        return new PromotionResult(0.0,true);
    }
}
