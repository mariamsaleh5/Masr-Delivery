package org.example;

public class PercentagePromotion implements PromotionStrategy {

    private final double percentage;
    private final double maxDiscount;

    public PercentagePromotion(
            double percentage,
            double maxDiscount
    ) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException(
                    "Percentage must be between 0 and 100."
            );
        }

        if (maxDiscount < 0) {
            throw new IllegalArgumentException(
                    "Maximum discount cannot be negative."
            );
        }

        this.percentage = percentage;
        this.maxDiscount = maxDiscount;
    }

    @Override
    public PromotionResult apply(
            double subtotal,
            double deliveryFee
    ) {

        double discount =
                subtotal * percentage / 100.0;

        discount = Math.min(
                discount,
                maxDiscount
        );

        return new PromotionResult(
                discount,
                false
        );
    }
}