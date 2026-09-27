package org.example;

import java.util.List;

public class ComboItem extends MenuItem {

    private final List<MenuItem> items;
    private final double discountRate;

    public ComboItem(
            int id,
            String name,
            double price,
            String category,
            int preparationTime,
            boolean available,
            List<MenuItem> items,
            double discountRate
    ) {
        super(
                id,
                name,
                price,
                category,
                preparationTime,
                available
        );

        this.items = items;
        this.discountRate = discountRate;
    }

    @Override
    public double calculatePrice(double quantity) {
        return getPrice() * quantity;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}