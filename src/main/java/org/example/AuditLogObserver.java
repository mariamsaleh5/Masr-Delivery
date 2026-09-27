package org.example;

public class AuditLogObserver
        implements OrderObserver {

    @Override
    public void onOrderStatusChanged(
            Order order,
            OrderStatus oldStatus,
            OrderStatus newStatus
    ) {

        System.out.println(
                "Audit Log: Order "
                        + order.getId()
                        + " changed from "
                        + oldStatus
                        + " to "
                        + newStatus
        );
    }
}