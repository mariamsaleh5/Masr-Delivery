package org.example;

public class FixedAmountPromotion implements PromotionStrategy{

    private double amount;
    FixedAmountPromotion(double amount){
        this.amount=amount;
    }

    @Override
    public PromotionResult apply(double subtotal, double deliveryFee) {
        return new PromotionResult(Math.min(amount,subtotal),false) ;
    }
}
