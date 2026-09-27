package org.example;

public class RiderDashboardObserver implements OrderObserver {

    @Override
    public void onOrderStatusChanged(
            Order order,
            OrderStatus oldStatus,
            OrderStatus newStatus
    ) {
        System.out.println(
                "Rider dashboard updated for Order "
                        + order.getId()
                        + ": "
                        + oldStatus
                        + " -> "
                        + newStatus
        );
    }
}