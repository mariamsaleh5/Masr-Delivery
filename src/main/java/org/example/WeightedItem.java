package org.example;

public class WeightedItem extends MenuItem {

    public WeightedItem(
            int id,
            String name,
            double pricePerKg,
            String category,
            int preparationTime,
            boolean available
    ) {
        super(
                id,
                name,
                pricePerKg,
                category,
                preparationTime,
                available
        );
    }

    @Override
    public double calculatePrice(double kilograms) {
        return getPrice() * kilograms;
    }
}
