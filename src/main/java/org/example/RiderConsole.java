package org.example;

import java.util.Scanner;

public class RiderConsole {

    private final RestaurantSystem system;
    private final Scanner scanner;
    private final Rider rider;

    public RiderConsole(
            RestaurantSystem system,
            Scanner scanner,
            Rider rider
    ) {
        this.system = system;
        this.scanner = scanner;
        this.rider = rider;
    }

    public void start() {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("===== Rider Area =====");

            System.out.println("1. Go On Duty");
            System.out.println("2. Go Off Duty");
            System.out.println("3. View Current Order");
            System.out.println("4. Pick Up Order");
            System.out.println("5. Mark Order Delivered");
            System.out.println("6. View Delivery Statistics");
            System.out.println("0. Back");

            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1:
                        goOnDuty();
                        break;

                    case 2:
                        goOffDuty();
                        break;

                    case 3:
                        viewCurrentOrder();
                        break;

                    case 4:
                        pickUpOrder();
                        break;

                    case 5:
                        markDelivered();
                        break;

                    case 6:
                        viewDeliveryStatistics();
                        break;

                    case 0:
                        running = false;
                        break;

                    default:
                        System.out.println(
                                "Invalid option."
                        );
                }

            } catch (PlatformException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );

            } catch (IllegalStateException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    private void goOnDuty() {

        system.goOnDuty(rider.getId());

        System.out.println(
                "Rider is now ON DUTY."
        );
    }

    private void goOffDuty() {

        system.goOffDuty(rider.getId());

        System.out.println(
                "Rider is now OFF DUTY."
        );
    }

    private void viewCurrentOrder() {

        System.out.println();
        System.out.println("===== Current Order =====");

        Order order =
                system.getRiderActiveOrder(
                        rider.getId()
                );

        if (order == null) {

            System.out.println(
                    "No active order."
            );

            return;
        }

        System.out.println(
                "Order Reference: " +
                        order.getId()
        );

        System.out.println(
                "Customer: " +
                        order.getCustomer().getName()
        );

        System.out.println(
                "Restaurant: " +
                        order.getRestaurant().getName()
        );

        System.out.println(
                "Delivery Address: " +
                        order.getDeliveryAddress()
        );

        System.out.println(
                "Status: " +
                        order.getStatus()
        );

        System.out.println(
                "Total: " +
                        order.getTotal() +
                        " EGP"
        );
    }

    private void pickUpOrder() {

        system.markOrderPickedUp(
                rider.getId()
        );

        System.out.println(
                "Order picked up successfully."
        );
    }

    private void markDelivered() {

        system.markOrderDelivered(
                rider.getId()
        );

        System.out.println(
                "Order delivered successfully."
        );
    }

    private void viewDeliveryStatistics() {

        System.out.println();
        System.out.println(
                "===== Delivery Statistics ====="
        );

        System.out.println(
                "Rider: " +
                        rider.getName()
        );

        System.out.println(
                "Status: " +
                        rider.getAvailabilityStatus()
        );

        System.out.println(
                "Completed Deliveries: " +
                        rider.getCompletedDeliveries()
        );

        System.out.println(
                "Average Delivery Time: " +
                        rider.getAverageDeliveryDuration() +
                        " minutes"
        );
    }
}