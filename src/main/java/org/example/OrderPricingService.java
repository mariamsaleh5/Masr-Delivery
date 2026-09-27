package org.example;

public class OrderPricingService {

    private final DeliveryFeeCalculator deliveryFeeCalculator;
    private final ServiceFeeCalculator serviceFeeCalculator;
    private final PromotionContext promotionContext;

    public OrderPricingService(
            DeliveryFeeCalculator deliveryFeeCalculator,
            ServiceFeeCalculator serviceFeeCalculator,
            PromotionContext promotionContext
    ) {
        this.deliveryFeeCalculator = deliveryFeeCalculator;
        this.serviceFeeCalculator = serviceFeeCalculator;
        this.promotionContext = promotionContext;
    }

    public double calcTotal(double distanceKm, double subtotal) {

        double delivery = deliveryFeeCalculator.calculate(distanceKm);

        double service = serviceFeeCalculator.calculate(subtotal);

        PromotionResult promotion =
                promotionContext.applyPromotion(subtotal, delivery);

        if (promotion.isFreeDelivery()) {
            delivery = 0;
        }

        double total =
                subtotal
                        + delivery
                        + service
                        - promotion.getDiscount();

        return total;
    }
    public double calculateDelivery(double distanceKm) {
        return deliveryFeeCalculator.calculate(distanceKm);
    }

    public double calculateService(double subtotal) {
        return serviceFeeCalculator.calculate(subtotal);
    }

    public PromotionResult applyPromotion(
            double subtotal,
            double deliveryFee) {

        return promotionContext.applyPromotion(
                subtotal,
                deliveryFee
        );
    }
    public void setPromotionStrategy(
            PromotionStrategy promotionStrategy
    ) {
        promotionContext.setPromotionStrategy(
                promotionStrategy
        );
    }
}