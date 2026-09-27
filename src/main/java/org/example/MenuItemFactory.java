package org.example;

import java.util.List;

public class MenuItemFactory {

    public static MenuItem create(
            String type,
            int id,
            String name,
            double price,
            String category,
            int preparationTime,
            boolean available,
            List<MenuItem> items,
            double discountRate
    ) {

        switch (type.toUpperCase()) {

            case "STANDARD":
                return new StandardItem(
                        id,
                        name,
                        price,
                        category,
                        preparationTime,
                        available
                );

            case "WEIGHTED":
                return new WeightedItem(
                        id,
                        name,
                        price,
                        category,
                        preparationTime,
                        available
                );

            case "COMBO":
                return new ComboItem(
                        id,
                        name,
                        price,
                        category,
                        preparationTime,
                        available,
                        items,
                        discountRate
                );

            default:
                throw new IllegalArgumentException(
                        "Unknown menu item type: " + type
                );
        }
    }
}