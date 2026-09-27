package org.example;

public class StatisticsObserver implements OrderObserver {

    @Override
    public void onOrderStatusChanged(
            Order order,
            OrderStatus oldStatus,
            OrderStatus newStatus
    ) {
        System.out.println(
                "Statistics recalculated because Order "
                        + order.getId()
                        + " changed to "
                        + newStatus
        );
    }
}