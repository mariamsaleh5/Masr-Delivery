package org.example;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Customer {
    private int id;
    private String name;
    private String mobile;
    private  List<String>address;
    private double balance;

    private int completedOrderCount;

    public Customer(int id, String name, String mobile,  double balance) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.balance = balance;
        this.address = new ArrayList<>();
        this.completedOrderCount = 0;
    }
    public LoyaltyTier getLoyaltyTier() {

        if (completedOrderCount >= 30) {
            return LoyaltyTier.GOLD;
        }

        if (completedOrderCount >= 10) {
            return LoyaltyTier.SILVER;
        }

        return LoyaltyTier.BRONZE;
    }
    public double applyDeliveryBenefit(double deliveryFee) {

        switch (getLoyaltyTier()) {

            case BRONZE:
                return deliveryFee;

            case SILVER:
                return deliveryFee * 0.90;

            case GOLD:
                return 0.0;

            default:
                throw new IllegalStateException("Unknown loyalty tier");
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public List<String> getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }
    public void deductBalance(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero."
            );
        }

        if (balance < amount) {
            throw new IllegalStateException(
                    "Insufficient funds."
            );
        }

        balance -= amount;
    }
    public void refundBalance(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Refund amount must be greater than zero."
            );
        }

        balance += amount;
    }
    public int getCompletedOrderCount() {
        return completedOrderCount;
    }
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Customer)) {
            return false;
        }

        Customer customer = (Customer) o;

        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}
