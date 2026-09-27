package org.example;

public class PromotionContext {

    private PromotionStrategy promotionStrategy;

    public PromotionContext() {
        this.promotionStrategy = null;
    }

    public PromotionContext(
            PromotionStrategy promotionStrategy
    ) {
        this.promotionStrategy = promotionStrategy;
    }

    public void setPromotionStrategy(
            PromotionStrategy promotionStrategy
    ) {
        this.promotionStrategy = promotionStrategy;
    }

    public PromotionResult applyPromotion(
            double subtotal,
            double deliveryFee
    ) {
        if (promotionStrategy == null) {
            return new PromotionResult(0, false);
        }
        return promotionStrategy.apply(
                subtotal,
                deliveryFee
        );
    }
}