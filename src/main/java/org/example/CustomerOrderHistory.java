package org.example;
import java.util.Comparator;
import java.util.List;
public class CustomerOrderHistory {
    private final Customer customer;
    private final List<Order> orders;
    private final double totalSpent;

    public CustomerOrderHistory(
            Customer customer,
            List<Order> orders,
            double totalSpent
    ) {
        this.customer = customer;
        this.orders = orders;
        this.totalSpent = totalSpent;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Order> getOrders() {
        return List.copyOf(orders);
    }

    public double getTotalSpent() {
        return totalSpent;
    }
}
