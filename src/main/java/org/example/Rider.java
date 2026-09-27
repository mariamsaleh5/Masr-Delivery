package org.example;

import java.util.Objects;
import java.time.LocalDateTime;

public class Rider {

    private final int id;
    private final String name;
    private final VehicleType vehicleType;

    // NEW
    private final DispatchStrategy dispatchStrategy;

    private String currentDistrict;
    private AvailabilityStatus availabilityStatus;
    private int completedDeliveries;
    private Order active_order;
    private LocalDateTime deliveryStartedAt;
    private long totalDeliveryMinutes;

    public Rider(
            int id,
            String name,
            VehicleType vehicleType,
            String currentDistrict,
            DispatchStrategy dispatchStrategy
    ) {
        this.id = id;
        this.name = name;
        this.vehicleType = vehicleType;
        this.currentDistrict = currentDistrict;
        this.dispatchStrategy = dispatchStrategy;

        this.availabilityStatus = AvailabilityStatus.AVAILABLE;
        this.completedDeliveries = 0;
        this.active_order = null;
        this.totalDeliveryMinutes = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getCurrentDistrict() {
        return currentDistrict;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public int getCompletedDeliveries() {
        return completedDeliveries;
    }


    public boolean canHandleOrder(Order order) {
        return dispatchStrategy.canHandleOrder(order);
    }


    public double calculateDeliveryTime(double distanceKm) {
        return dispatchStrategy.calculateDeliveryTime(distanceKm);
    }

    public boolean hasActiveOrder() {
        return active_order != null;
    }

    public void assignOrder(Order order) {

        if (active_order != null) {
            throw new IllegalStateException(
                    "Rider already has an active order"
            );
        }

        active_order = order;
        deliveryStartedAt = null;
    }

    public void completeCurrentOrder(long deliveryMinutes) {

        if (active_order == null) {
            throw new IllegalStateException(
                    "Rider has no active order"
            );
        }

        totalDeliveryMinutes += deliveryMinutes;
        completedDeliveries++;
        active_order = null;
    }

    public double getAverageDeliveryDuration() {

        if (completedDeliveries == 0) {
            return 0.0;
        }

        return (double) totalDeliveryMinutes / completedDeliveries;
    }
    public Order getActiveOrder() {
        return active_order;
    }
    public void goOnDuty() {
        availabilityStatus = AvailabilityStatus.AVAILABLE;
    }
    public void goOffDuty() {

        if (active_order != null) {
            throw new IllegalStateException(
                    "Rider cannot go off duty while having an active order."
            );
        }

        availabilityStatus = AvailabilityStatus.UNAVAILABLE;
    }
    public void pickUpOrder() {

        if (active_order == null) {
            throw new IllegalStateException(
                    "Rider has no active order"
            );
        }

        deliveryStartedAt = LocalDateTime.now();
    }

    public LocalDateTime getDeliveryStartedAt() {
        return deliveryStartedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rider rider = (Rider) o;

        return id == rider.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}