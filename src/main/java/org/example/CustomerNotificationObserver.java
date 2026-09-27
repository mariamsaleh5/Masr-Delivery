package org.example;

public class CustomerNotificationObserver implements OrderObserver {
    @Override
    public void onOrderStatusChanged(
            Order order,
            OrderStatus oldStatus,
            OrderStatus newStatus
    ) {

        System.out.println(
                "Customer notified: Order "
                        + order.getId()
                        + " changed from "
                        + oldStatus
                        + " to "
                        + newStatus
        );
    }
}
