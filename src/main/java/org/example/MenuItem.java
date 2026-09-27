package org.example;

public abstract class MenuItem {

    private final int id;
    private final String name;
    private final double price;
    private final String category;
    private final int preparationTime;
    private boolean available;
    private int dailyStock;

    public MenuItem(
            int id,
            String name,
            double price,
            String category,
            int preparationTime,
            boolean available
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.preparationTime = preparationTime;
        this.available = available;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    public int getDailyStock() {
        return dailyStock;
    }

    public void setDailyStock(int dailyStock) {

        if (dailyStock < 0) {
            throw new IllegalArgumentException(
                    "Stock cannot be negative."
            );
        }

        this.dailyStock = dailyStock;
    }

    public abstract double calculatePrice(double quantity);
}