package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class CustomerConsole {

    private final Scanner scanner;
    private final RestaurantSystem system;
    private final Customer customer;

    public CustomerConsole(
            Scanner scanner,
            RestaurantSystem system,
            Customer customer
    ) {
        this.scanner = scanner;
        this.system = system;
        this.customer = customer;
    }

// =========================
// CUSTOMER MAIN MENU
// =========================

    public void showMenu() {

        boolean back = false;

        while (!back) {

            System.out.println();
            System.out.println("============================================");
            System.out.println("              CUSTOMER MENU");
            System.out.println("============================================");
            System.out.println("1. Browse restaurants");
            System.out.println("2. Search restaurants");
            System.out.println("3. View menu");
            System.out.println("4. Place order");
            System.out.println("5. Pay from wallet");
            System.out.println("6. Track order");
            System.out.println("7. Cancel order");
            System.out.println("8. Order history");
            System.out.println("0. Back");
            System.out.println("============================================");

            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    browseRestaurants();
                    break;

                case 2:
                    searchRestaurants();
                    break;

                case 3:
                    viewMenu();
                    break;

                case 4:
                    placeOrder();
                    break;

                case 5:
                    payFromWallet();
                    break;

                case 6:
                    trackOrder();
                    break;

                case 7:
                    cancelOrder();
                    break;

                case 8:
                    orderHistory();
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }


    private void browseRestaurants() {

        System.out.println();
        System.out.println("===== Browse Restaurants =====");

        System.out.print("District (Enter to skip): ");
        String district = scanner.nextLine();

        System.out.print("Cuisine (Enter to skip): ");
        String cuisine = scanner.nextLine();

        System.out.print("Minimum rating (Enter to skip): ");
        String ratingInput = scanner.nextLine();

        Double minimumRating = null;

        if (!ratingInput.isBlank()) {
            minimumRating = Double.parseDouble(ratingInput);
        }

        List<Restaurant> restaurants =
                system.browseRestaurants(
                        district,
                        cuisine,
                        minimumRating
                );

        if (restaurants.isEmpty()) {
            System.out.println("No restaurants found.");
            return;
        }

        System.out.println();
        System.out.println("Available Restaurants:");

        for (int i = 0; i < restaurants.size(); i++) {

            Restaurant restaurant = restaurants.get(i);

            System.out.println(
                    (i + 1)
                            + ". "
                            + restaurant.getName()
                            + " | District: "
                            + restaurant.getDistrict()
                            + " | Cuisine: "
                            + restaurant.getCuisine()
                            + " | Rating: "
                            + restaurant.getRating()
            );
        }
    }


    private void searchRestaurants() {

        System.out.println();
        System.out.println("===== Search Restaurants =====");

        System.out.print("Search: ");
        String searchText = scanner.nextLine();

        if (searchText.isBlank()) {
            System.out.println("Search text cannot be empty.");
            return;
        }

        List<Restaurant> restaurants =
                system.searchRestaurants(searchText);

        if (restaurants.isEmpty()) {
            System.out.println("Nothing found.");
            return;
        }

        System.out.println();
        System.out.println("Matching restaurants:");

        for (int i = 0; i < restaurants.size(); i++) {

            Restaurant restaurant = restaurants.get(i);

            System.out.println(
                    (i + 1)
                            + ". "
                            + restaurant.getName()
                            + " | "
                            + restaurant.getCuisine()
                            + " | Rating: "
                            + restaurant.getRating()
            );
        }
    }



    private void viewMenu() {

        System.out.println();
        System.out.println("===== View Menu =====");

        System.out.print("Restaurant number: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();

        Restaurant restaurant =
                system.getRestaurant(restaurantId);

        if (restaurant == null) {
            System.out.println("Restaurant not found.");
            return;
        }

        System.out.println();
        System.out.println("===== " + restaurant.getName() + " Menu =====");

        List<MenuItem> menu = restaurant.getMenu();

        if (menu.isEmpty()) {
            System.out.println("No menu items found.");
            return;
        }

        for (int i = 0; i < menu.size(); i++) {

            MenuItem item = menu.get(i);

            System.out.print(
                    (i + 1)
                            + ". "
                            + item.getName()
                            + " - "
                            + item.getPrice()
                            + " EGP"
            );

            if (!item.isAvailable()) {
                System.out.print(" [UNAVAILABLE]");
            }

            System.out.println();
        }
    }


// =========================
// 4. PLACE ORDER
// =========================


    private void placeOrder() {

        System.out.println();
        System.out.println("===== Place Order =====");



        System.out.print("Restaurant number: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();

        Restaurant restaurant =
                system.getRestaurant(restaurantId);

        if (restaurant == null) {
            System.out.println("Restaurant not found.");
            return;
        }

        System.out.println(
                "Restaurant selected: "
                        + restaurant.getName()
        );



        List<Order_item> items =
                new java.util.ArrayList<>();

        boolean addingItems = true;

        while (addingItems) {

            System.out.print("Menu item number: ");
            int itemNumber = scanner.nextInt();

            System.out.print("Quantity: ");
            double quantity = scanner.nextDouble();
            scanner.nextLine();

            List<MenuItem> menu =
                    restaurant.getMenu();

            if (itemNumber < 1 ||
                    itemNumber > menu.size()) {

                System.out.println(
                        "Invalid menu item."
                );

                continue;
            }

            MenuItem menuItem =
                    menu.get(itemNumber - 1);

            if (!menuItem.isAvailable()) {

                System.out.println(
                        "Item is unavailable."
                );

                continue;
            }

            Order_item orderItem =
                    new Order_item(
                            menuItem,
                            quantity
                    );

            items.add(orderItem);

            System.out.print(
                    "Add another item? (y/n): "
            );

            String answer =
                    scanner.nextLine();

            if (!answer.equalsIgnoreCase("y")) {
                addingItems = false;
            }
        }




        if (items.isEmpty()) {

            System.out.println(
                    "Order must contain at least one item."
            );

            return;
        }



        System.out.println();
        System.out.println("Delivery address");

        System.out.print("District: ");
        String district =
                scanner.nextLine();

        System.out.print("Street: ");
        String street =
                scanner.nextLine();

        Address address =
                new Address(
                        district,
                        street
                );



        System.out.print(
                "Distance in km: "
        );

        double distanceKm =
                scanner.nextDouble();

        scanner.nextLine();


        System.out.print(
                "Promotion code (optional): "
        );

        String promoCode =
                scanner.nextLine();

        if (promoCode.isBlank()) {
            promoCode = null;
        }




        try {

            Order order =
                    system.placeOrder(
                            customer,
                            restaurant,
                            items,
                            address,
                            promoCode,
                            distanceKm
                    );



            System.out.println();
            System.out.println(
                    "========== ORDER SUMMARY =========="
            );

            System.out.println(
                    "Order ID: "
                            + order.getId()
            );

            System.out.println(
                    "Restaurant: "
                            + order.getRestaurant()
                            .getName()
            );

            System.out.println(
                    "Subtotal: "
                            + order.getSubtotal()
                            + " EGP"
            );

            System.out.println(
                    "Delivery fee: "
                            + order.getDeliveryFee()
                            + " EGP"
            );

            System.out.println(
                    "Service fee: "
                            + order.getServiceFee()
                            + " EGP"
            );

            System.out.println(
                    "Discount: "
                            + order.getDiscount()
                            + " EGP"
            );

            System.out.println(
                    "Total: "
                            + order.getTotal()
                            + " EGP"
            );

            System.out.println(
                    "==================================="
            );


        } catch (PlatformException e) {

            System.out.println();

            System.out.println(
                    "Order rejected: "
                            + e.getMessage()
            );
        }
    }


// =========================
// 5. PAY FROM WALLET
// =========================

    private void payFromWallet() {

        System.out.println();
        System.out.println("===== Pay From Wallet =====");

        System.out.print("Order reference: ");

        int orderId = scanner.nextInt();

        scanner.nextLine();
        Order order= system.getOrder(orderId);
        try {

            system.payFromWallet(
                    customer,order

            );

            System.out.println(
                    "Payment successful."
            );

            System.out.println(
                    "New wallet balance: "
                            + customer.getBalance()
                            + " EGP"
            );

        } catch (InsufficientBalanceException e) {

            System.out.println(
                    "Insufficient funds: "
                            + e.getMessage()
            );

        } catch (PlatformException e) {

            System.out.println(
                    "Payment failed: "
                            + e.getMessage()
            );
        }
    }




    private void trackOrder() {

        System.out.println();
        System.out.println("===== Track Order =====");

        System.out.print("Order reference: ");

        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order order =
                system.getOrder(orderId);

        if (order == null) {

            System.out.println(
                    "Order not found."
            );

            return;
        }

        System.out.println();
        System.out.println(
                "========== ORDER TRACKING =========="
        );

        System.out.println(
                "Order Reference: "
                        + order.getId()
        );

        System.out.println(
                "Restaurant: "
                        + order.getRestaurant().getName()
        );

        System.out.println(
                "Status: "
                        + order.getStatus()
        );

        System.out.println(
                "Placed At: "
                        + order.getPlacedAt()
        );

        System.out.println(
                "Elapsed Time: "
                        + order.getElapsedTime()
        );

        System.out.println(
                "===================================="
        );
    }

    private void cancelOrder() {

        System.out.println();
        System.out.println("===== Cancel Order =====");

        System.out.print("Order reference: ");

        int orderId =
                scanner.nextInt();

        scanner.nextLine();

        try {

            Order order =
                    system.getOrder(orderId);

            if (order == null) {

                System.out.println(
                        "Order not found."
                );

                return;
            }

            double refundAmount =
                    order.getTotal();

            system.cancelOrder(
                    customer.getId(),
                    orderId
            );

            System.out.println();
            System.out.println(
                    "Order cancelled successfully."
            );

            System.out.println(
                    "Refunded: "
                            + refundAmount
                            + " EGP"
            );

            System.out.println(
                    "New wallet balance: "
                            + customer.getBalance()
                            + " EGP"
            );

        } catch (PlatformException e) {

            System.out.println();

            System.out.println(
                    "Cancellation not permitted: "
                            + e.getMessage()
            );
        }
    }
    private void orderHistory() {

        System.out.println();
        System.out.println("===== Order History =====");

        List<Order> history =system.getCustomerOrderHistory(customer.getId());


        if (history.isEmpty()) {

            System.out.println(
                    "No orders found."
            );

            return;
        }

        System.out.println();

        for (Order order : history) {

            System.out.println(
                    "Order Reference: "
                            + order.getId()
            );

            System.out.println(
                    "Restaurant: "
                            + order.getRestaurant().getName()
            );

            System.out.println(
                    "Placed At: "
                            + order.getPlacedAt()
            );

            System.out.println(
                    "Status: "
                            + order.getStatus()
            );

            System.out.println(
                    "Total: "
                            + order.getTotal()
                            + " EGP"
            );

            System.out.println(
                    "--------------------------------"
            );
        }

        double lifetimeTotal =
                system.getCustomerLifetimeSpent(
                        customer.getId()
                );

        System.out.println();
        System.out.println(
                "Lifetime Total Spent: "
                        + lifetimeTotal
                        + " EGP"
        );

        System.out.println(
                "==============================="
        );
    }

}
