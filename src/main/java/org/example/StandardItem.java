package org.example;

public class StandardItem extends MenuItem {

    public StandardItem(
            int id,
            String name,
            double price,
            String category,
            int preparationTime,
            boolean available
    ) {
        super(
                id,
                name,
                price,
                category,
                preparationTime,
                available
        );
    }

    @Override
    public double calculatePrice(double quantity) {
        return getPrice() * quantity;
    }
}