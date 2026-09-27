package org.example;

import java.util.*;

public class Restaurant {

    private int id;
    private String name;
    private String district;
    private String cuisine;
    private double rating;
    private res_Status resStatus;

    private final Map<Integer, MenuItem> menu = new LinkedHashMap<>();

    public Restaurant(int id, String name, String district,
                      double rating, String cuisine) {

        this.id = id;
        this.name = name;
        this.district = district;
        this.rating = rating;
        this.resStatus = res_Status.Open;
        this.cuisine = cuisine;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDistrict() {
        return district;
    }

    public String getCuisine() {
        return cuisine;
    }

    public double getRating() {
        return rating;
    }

    public res_Status getResStatus() {
        return resStatus;
    }

    public boolean isOpen() {
        return resStatus == res_Status.Open;
    }

    // Add menu item
    public void addMenuItem(MenuItem m) {

        if (menu.containsKey(m.getId())) {
            throw new IllegalArgumentException(
                    "Menu item ID already exists."
            );
        }

        menu.put(m.getId(), m);
    }
    // Get menu in insertion order
    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu.values());
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Restaurant that = (Restaurant) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}