package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private final int id;
    private final Customer customer;
    private final Restaurant restaurant;
    private final Address deliveryAddress;
    private final LocalDateTime placedAt;

    private final List<Order_item> items;

    // =========================
    // PRICING
    // =========================

    private final double subtotal;
    private final double deliveryFee;
    private final double serviceFee;
    private final double discount;
    private final double total;

    // =========================
    // STATUS
    // =========================

    private OrderStatus status;

    // =========================
    // OBSERVERS
    // =========================

    private final List<OrderObserver> observers =
            new ArrayList<>();


    // =========================
    // CONSTRUCTOR
    // =========================

    private Order(Builder builder) {

        if (builder.items == null ||
                builder.items.isEmpty()) {

            throw new IllegalArgumentException(
                    "Order must contain at least one item"
            );
        }

        this.id = builder.id;
        this.customer = builder.customer;
        this.restaurant = builder.restaurant;
        this.deliveryAddress = builder.deliveryAddress;

        this.items =
                new ArrayList<>(builder.items);

        this.subtotal = builder.subtotal;
        this.deliveryFee = builder.deliveryFee;
        this.serviceFee = builder.serviceFee;
        this.discount = builder.discount;
        this.total = builder.total;

        this.placedAt = LocalDateTime.now();

        this.status = OrderStatus.PLACED;
    }


    // =========================
    // BUILDER
    // =========================

    public static class Builder {

        private int id;
        private Customer customer;
        private Restaurant restaurant;
        private Address deliveryAddress;

        private final List<Order_item> items =
                new ArrayList<>();

        private double subtotal;
        private double deliveryFee;
        private double serviceFee;
        private double discount;
        private double total;


        public Builder id(int id) {
            this.id = id;
            return this;
        }


        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }


        public Builder restaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            return this;
        }


        public Builder deliveryAddress(Address deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }


        public Builder addItem(Order_item item) {

            if (item == null) {
                throw new IllegalArgumentException(
                        "Order item cannot be null"
                );
            }

            this.items.add(item);

            return this;
        }


        public Builder subtotal(double subtotal) {
            this.subtotal = subtotal;
            return this;
        }


        public Builder deliveryFee(double deliveryFee) {
            this.deliveryFee = deliveryFee;
            return this;
        }


        public Builder serviceFee(double serviceFee) {
            this.serviceFee = serviceFee;
            return this;
        }


        public Builder discount(double discount) {
            this.discount = discount;
            return this;
        }


        public Builder total(double total) {
            this.total = total;
            return this;
        }


        public Order build() {
            return new Order(this);
        }
    }


    // =========================
    // GETTERS
    // =========================

    public int getId() {
        return id;
    }


    public Customer getCustomer() {
        return customer;
    }


    public Restaurant getRestaurant() {
        return restaurant;
    }


    public Address getDeliveryAddress() {
        return deliveryAddress;
    }


    public LocalDateTime getPlacedAt() {
        return placedAt;
    }


    public List<Order_item> getItems() {
        return List.copyOf(items);
    }


    public OrderStatus getStatus() {
        return status;
    }


    public double getSubtotal() {
        return subtotal;
    }


    public double getDeliveryFee() {
        return deliveryFee;
    }


    public double getServiceFee() {
        return serviceFee;
    }


    public double getDiscount() {
        return discount;
    }


    public double getTotal() {
        return total;
    }


    // =========================
    // ORDER STATUS
    // =========================

    public void accept() {

        if (status != OrderStatus.PLACED) {

            throw new IllegalOrderTransitionException(
                    "Order can only be accepted when it is PLACED"
            );
        }

        changeStatus(OrderStatus.ACCEPTED);
    }


    public void startPreparing() {

        if (status != OrderStatus.ACCEPTED) {

            throw new IllegalOrderTransitionException(
                    "Order can only start preparing when it is ACCEPTED"
            );
        }

        changeStatus(OrderStatus.PREPARING);
    }


    public void markReady() {

        if (status != OrderStatus.PREPARING) {

            throw new IllegalOrderTransitionException(
                    "Order can only be READY when it is PREPARING"
            );
        }

        changeStatus(OrderStatus.READY);
    }


    public void assignRider() {

        if (status != OrderStatus.READY) {

            throw new IllegalOrderTransitionException(
                    "Order can only be assigned when it is READY"
            );
        }

        changeStatus(OrderStatus.ASSIGNED);
    }


    public void startDelivery() {

        if (status != OrderStatus.ASSIGNED) {

            throw new IllegalOrderTransitionException(
                    "Order can only go out for delivery when it is ASSIGNED"
            );
        }

        changeStatus(OrderStatus.OUT_FOR_DELIVERY);
    }


    public void deliver() {

        if (status != OrderStatus.OUT_FOR_DELIVERY) {

            throw new IllegalOrderTransitionException(
                    "Order can only be delivered when it is OUT_FOR_DELIVERY"
            );
        }

        changeStatus(OrderStatus.DELIVERED);
    }


    public void cancel() {

        if (status == OrderStatus.OUT_FOR_DELIVERY ||
                status == OrderStatus.DELIVERED ||
                status == OrderStatus.CANCELLED) {

            throw new IllegalOrderTransitionException(
                    "Order cannot be cancelled in its current state"
            );
        }

        changeStatus(OrderStatus.CANCELLED);
    }


    // =========================
    // STATUS CHANGE
    // =========================

    private void changeStatus(OrderStatus newStatus) {

        OrderStatus oldStatus = this.status;

        this.status = newStatus;

        notifyObservers(
                oldStatus,
                newStatus
        );
    }


    // =========================
    // OBSERVER
    // =========================

    public void addObserver(OrderObserver observer) {

        if (observer == null) {
            throw new IllegalArgumentException(
                    "Observer cannot be null"
            );
        }

        observers.add(observer);
    }


    private void notifyObservers(
            OrderStatus oldStatus,
            OrderStatus newStatus
    ) {

        for (OrderObserver observer : observers) {

            observer.onOrderStatusChanged(
                    this,
                    oldStatus,
                    newStatus
            );
        }
    }


    public List<OrderObserver> getObservers() {
        return List.copyOf(observers);
    }


    public String getElapsedTime() {

        java.time.Duration duration =
                java.time.Duration.between(
                        placedAt,
                        java.time.LocalDateTime.now()
                );

        long hours = duration.toHours();

        long minutes =
                duration.toMinutes() % 60;

        long seconds =
                duration.getSeconds() % 60;

        return hours + "h "
                + minutes + "m "
                + seconds + "s";
    }
    public void reject() {

        if (status != OrderStatus.PLACED) {
            throw new IllegalOrderTransitionException(
                    "Order can only be rejected when it is PLACED"
            );
        }

        changeStatus(OrderStatus.CANCELLED);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null ||
                getClass() != o.getClass()) {

            return false;
        }

        Order order = (Order) o;

        return id == order.id;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}