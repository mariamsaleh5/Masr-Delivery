package org.example;

public class Order_item {
    private final MenuItem menuItem;

    private final double quantity;
    public Order_item(MenuItem menuItem, double quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }

        this.menuItem = menuItem;
        this.quantity = quantity;

    }
    public MenuItem getMenuItem() {
        return menuItem;
    }

    public double  getQuantity() {
        return quantity;
    }
}
