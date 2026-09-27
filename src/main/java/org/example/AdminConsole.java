package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class AdminConsole {

    private final RestaurantSystem system;
    private final Scanner scanner;

    public AdminConsole(
            RestaurantSystem system,
            Scanner scanner
    ) {
        this.system = system;
        this.scanner = scanner;
    }

    public void start() {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("===== Admin Area =====");

            System.out.println("1. Add Restaurant");
            System.out.println("2. Remove Restaurant");
            System.out.println("3. Create Promotion");
            System.out.println("4. Run Reports");
            System.out.println("5. Platform Statistics");
            System.out.println("6. Add Customer");
            System.out.println("7. Add Rider");
            System.out.println("0. Back");

            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1:
                        addRestaurant();
                        break;

                    case 2:
                        removeRestaurant();
                        break;

                    case 3:
                        createPromotion();
                        break;

                    case 4:
                        runReports();
                        break;

                    case 5:
                        viewPlatformStatistics();
                        break;
                        
                    case 6:
                        addCustomer();
                        break;
                        
                    case 7:
                        addRider();
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

            } catch (IllegalArgumentException e) {

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

    // =========================================================
    // ADD RESTAURANT
    // =========================================================

    private void addRestaurant() {

        System.out.println();
        System.out.println("===== Add Restaurant =====");

        System.out.print("Restaurant ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Restaurant name: ");
        String name = scanner.nextLine();

        System.out.print("District: ");
        String district = scanner.nextLine();

        System.out.print("Rating: ");
        double rating = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Cuisine: ");
        String cuisine = scanner.nextLine();

        Restaurant restaurant =
                new Restaurant(
                        id,
                        name,
                        district,
                        rating,
                        cuisine
                );

        system.addRestaurant(restaurant);

        System.out.println(
                "Restaurant added successfully."
        );
    }
    
    // =========================================================
    // ADD CUSTOMER
    // =========================================================

    private void addCustomer() {

        System.out.println();
        System.out.println("===== Add Customer =====");

        System.out.print("Customer ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Customer Name: ");
        String name = scanner.nextLine();

        System.out.print("Mobile: ");
        String mobile = scanner.nextLine();

        System.out.print("Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine();

        Customer customer = new Customer(id, name, mobile, balance);
        system.addCustomer(customer);

        System.out.println("Customer added successfully.");
    }
    
    // =========================================================
    // ADD RIDER
    // =========================================================

    private void addRider() {

        System.out.println();
        System.out.println("===== Add Rider =====");

        System.out.print("Rider ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Rider Name: ");
        String name = scanner.nextLine();

        System.out.print("Vehicle Type (1: MOTORCYCLE, 2: BICYCLE, 3: CAR): ");
        int type = scanner.nextInt();
        scanner.nextLine();

        System.out.print("District: ");
        String district = scanner.nextLine();

        VehicleType vehicleType = type == 1 ? VehicleType.MOTORCYCLE : type == 2 ? VehicleType.BICYCLE : VehicleType.CAR;
        DispatchStrategy ds = type == 1 ? new MotorcycleDispatchStrategy() : type == 2 ? new BicycleDispatchStrategy() : new CarDispatchStrategy();

        Rider rider = new Rider(id, name, vehicleType, district, ds);
        system.addRider(rider);

        System.out.println("Rider added successfully.");
    }

    // =========================================================
    // REMOVE RESTAURANT
    // =========================================================

    private void removeRestaurant() {

        System.out.println();
        System.out.println("===== Remove Restaurant =====");

        System.out.print("Restaurant ID: ");

        int restaurantId =
                scanner.nextInt();

        scanner.nextLine();

        system.removeRestaurant(restaurantId);

        System.out.println(
                "Restaurant removed successfully."
        );
    }

    // =========================================================
    // CREATE PROMOTION
    // =========================================================

    private void createPromotion() {

        System.out.println();
        System.out.println("===== Create Promotion =====");

        System.out.println("1. Percentage Discount");
        System.out.println("2. Fixed Amount Discount");
        System.out.println("3. Free Delivery");

        System.out.print("Choose promotion type: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {

            case 1:

                System.out.print(
                        "Discount percentage: "
                );

                double percentage =
                        scanner.nextDouble();

                System.out.print(
                        "Maximum discount: "
                );

                double maxDiscount =
                        scanner.nextDouble();

                scanner.nextLine();

                system.createPercentagePromotion(
                        percentage,
                        maxDiscount
                );

                System.out.println(
                        "Percentage promotion created successfully."
                );

                break;

            case 2:

                System.out.print(
                        "Discount amount: "
                );

                double amount =
                        scanner.nextDouble();

                scanner.nextLine();

                system.createFixedAmountPromotion(
                        amount
                );

                System.out.println(
                        "Fixed amount promotion created successfully."
                );

                break;

            case 3:

                system.createFreeDeliveryPromotion();

                System.out.println(
                        "Free delivery promotion created successfully."
                );

                break;

            default:

                System.out.println(
                        "Invalid promotion type."
                );
        }
    }

    // =========================================================
    // REPORTS MENU
    // =========================================================

    private void runReports() {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("===== Reports =====");

            System.out.println("1. Total Revenue");
            System.out.println("2. Top Five Restaurants By Revenue");
            System.out.println("3. Average Order Value By District");
            System.out.println("4. Highly Rated Restaurants");
            System.out.println("5. Orders Count By Status");
            System.out.println("6. Rider Delivery Report");
            System.out.println("7. Most Frequently Ordered Item");
            System.out.println("8. Customer Order History");
            System.out.println("9. Peak Ordering Hour");
            System.out.println("10. Customers With No Recent Orders");
            System.out.println("0. Back");

            System.out.print("Choose an option: ");

            int choice =
                    scanner.nextInt();

            scanner.nextLine();

            try {

                switch (choice) {

                    case 1:
                        viewTotalRevenue();
                        break;

                    case 2:
                        viewTopFiveRestaurants();
                        break;

                    case 3:
                        viewAverageOrderValueByDistrict();
                        break;

                    case 4:
                        viewHighlyRatedRestaurants();
                        break;

                    case 5:
                        viewOrdersCountByStatus();
                        break;

                    case 6:
                        viewRiderDeliveryReport();
                        break;

                    case 7:
                        viewMostFrequentlyOrderedItem();
                        break;

                    case 8:
                        viewCustomerOrderHistory();
                        break;

                    case 9:
                        viewPeakOrderingHour();
                        break;

                    case 10:
                        viewCustomersWithNoRecentOrders();
                        break;

                    case 0:
                        running = false;
                        break;

                    default:
                        System.out.println(
                                "Invalid option."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // =========================================================
    // REPORT 1
    // TOTAL REVENUE
    // =========================================================

    private void viewTotalRevenue() {

        System.out.println();
        System.out.println("===== Total Revenue =====");

        System.out.print(
                "From date (yyyy-MM-dd): "
        );

        String fromInput =
                scanner.nextLine();

        System.out.print(
                "To date (yyyy-MM-dd): "
        );

        String toInput =
                scanner.nextLine();

        LocalDate fromDate =
                LocalDate.parse(fromInput);

        LocalDate toDate =
                LocalDate.parse(toInput);

        if (toDate.isBefore(fromDate)) {

            throw new IllegalArgumentException(
                    "To date cannot be before from date."
            );
        }

        LocalDateTime from =
                fromDate.atStartOfDay();

        LocalDateTime to =
                toDate
                        .plusDays(1)
                        .atStartOfDay();

        double revenue =
                system.getTotalRevenue(
                        from,
                        to
                );

        System.out.println();

        System.out.println(
                "From: " + fromDate
        );

        System.out.println(
                "To: " + toDate
        );

        System.out.println(
                "Total Revenue: "
                        + revenue
                        + " EGP"
        );
    }

    // =========================================================
    // REPORT 2
    // TOP FIVE RESTAURANTS
    // =========================================================

    private void viewTopFiveRestaurants() {

        System.out.println();
        System.out.println(
                "===== Top Five Restaurants By Revenue ====="
        );

        System.out.print(
                "Enter month (yyyy-MM): "
        );

        String monthInput =
                scanner.nextLine();

        YearMonth month =
                YearMonth.parse(monthInput);

        Map<Restaurant, Double> report =
                system.getTopFiveRestaurantsByRevenue(
                        month
                );

        if (report.isEmpty()) {

            System.out.println(
                    "No delivered orders found for this month."
            );

            return;
        }

        int rank = 1;

        for (Map.Entry<Restaurant, Double> entry :
                report.entrySet()) {

            Restaurant restaurant =
                    entry.getKey();

            double revenue =
                    entry.getValue();

            System.out.println();

            System.out.println(
                    rank + ". " +
                            restaurant.getName()
            );

            System.out.println(
                    "District: " +
                            restaurant.getDistrict()
            );

            System.out.println(
                    "Revenue: " +
                            revenue +
                            " EGP"
            );

            rank++;
        }
    }

    // =========================================================
    // REPORT 3
    // AVERAGE ORDER VALUE BY DISTRICT
    // =========================================================

    private void viewAverageOrderValueByDistrict() {

        System.out.println();
        System.out.println(
                "===== Average Order Value By District ====="
        );

        Map<String, Double> report =
                system.getAverageOrderValuePerDistrict();

        if (report.isEmpty()) {

            System.out.println(
                    "No orders found."
            );

            return;
        }

        report.entrySet()
                .stream()
                .sorted(
                        Map.Entry.comparingByKey()
                )
                .forEach(entry -> {

                    System.out.println(
                            "District: " +
                                    entry.getKey()
                    );

                    System.out.println(
                            "Average Order Value: " +
                                    entry.getValue() +
                                    " EGP"
                    );

                    System.out.println(
                            "-------------------------"
                    );
                });
    }

    // =========================================================
    // REPORT 4
    // HIGHLY RATED RESTAURANTS
    // =========================================================

    private void viewHighlyRatedRestaurants() {

        System.out.println();
        System.out.println(
                "===== Highly Rated Restaurants ====="
        );

        List<Restaurant> restaurants =
                system.getHighlyRatedRestaurants();

        if (restaurants.isEmpty()) {

            System.out.println(
                    "No restaurants match the report criteria."
            );

            return;
        }

        restaurants.forEach(restaurant -> {

            System.out.println();

            System.out.println(
                    "Restaurant: " +
                            restaurant.getName()
            );

            System.out.println(
                    "District: " +
                            restaurant.getDistrict()
            );

            System.out.println(
                    "Cuisine: " +
                            restaurant.getCuisine()
            );

            System.out.println(
                    "Rating: " +
                            restaurant.getRating()
            );

            System.out.println(
                    "-------------------------"
            );
        });
    }

    // =========================================================
    // REPORT 5
    // ORDERS COUNT BY STATUS
    // =========================================================

    private void viewOrdersCountByStatus() {

        System.out.println();
        System.out.println(
                "===== Orders Count By Status ====="
        );

        Map<OrderStatus, Long> report =
                system.getOrdersCountByStatus();

        if (report.isEmpty()) {

            System.out.println(
                    "No orders found."
            );

            return;
        }

        report.entrySet()
                .stream()
                .forEach(entry -> {

                    System.out.println(
                            "Status: " +
                                    entry.getKey()
                    );

                    System.out.println(
                            "Count: " +
                                    entry.getValue()
                    );

                    System.out.println(
                            "-------------------------"
                    );
                });
    }

    // =========================================================
    // REPORT 6
    // RIDER DELIVERY REPORT
    // =========================================================

    private void viewRiderDeliveryReport() {

        System.out.println();
        System.out.println(
                "===== Rider Delivery Report ====="
        );

        List<Rider> riders =
                system.getRiderDeliveryReport();

        if (riders.isEmpty()) {

            System.out.println(
                    "No riders found."
            );

            return;
        }

        riders.forEach(rider -> {

            System.out.println();

            System.out.println(
                    "Rider: " +
                            rider.getName()
            );

            System.out.println(
                    "Completed Deliveries: " +
                            rider.getCompletedDeliveries()
            );

            System.out.println(
                    "Average Delivery Duration: " +
                            rider.getAverageDeliveryDuration() +
                            " minutes"
            );

            System.out.println(
                    "-------------------------"
            );
        });
    }

    // =========================================================
    // REPORT 7
    // MOST FREQUENTLY ORDERED ITEM
    // =========================================================

    private void viewMostFrequentlyOrderedItem() {

        System.out.println();
        System.out.println(
                "===== Most Frequently Ordered Item ====="
        );

        Optional<MenuItem> result =
                system.getMostFrequentlyOrderedItem();

        if (result.isEmpty()) {

            System.out.println(
                    "No orders exist, so there is no most frequently ordered item."
            );

            return;
        }

        MenuItem item =
                result.get();

        System.out.println(
                "Item: " +
                        item.getName()
        );

        System.out.println(
                "Category: " +
                        item.getCategory()
        );

        System.out.println(
                "Price: " +
                        item.getPrice() +
                        " EGP"
        );
    }

    // =========================================================
    // REPORT 8
    // CUSTOMER ORDER HISTORY
    // =========================================================

    private void viewCustomerOrderHistory() {

        System.out.println();
        System.out.println(
                "===== Customer Order History ====="
        );

        System.out.print(
                "Customer ID: "
        );

        int customerId =
                scanner.nextInt();

        scanner.nextLine();

        List<Order> history =
                system.getCustomerOrderHistory(
                        customerId
                );

        if (history.isEmpty()) {

            System.out.println(
                    "No orders found for this customer."
            );

            return;
        }

        System.out.println();

        System.out.println(
                "===== Order History ====="
        );

        for (Order order : history) {

            System.out.println();

            System.out.println(
                    "Order ID: " +
                            order.getId()
            );

            System.out.println(
                    "Restaurant: " +
                            order.getRestaurant()
                                    .getName()
            );

            System.out.println(
                    "Status: " +
                            order.getStatus()
            );

            System.out.println(
                    "Order Date: " +
                            order.getPlacedAt()
            );

            System.out.println(
                    "Total: " +
                            order.getTotal() +
                            " EGP"
            );

            System.out.println(
                    "-------------------------"
            );
        }

        double totalSpent =
                system.getCustomerLifetimeSpent(
                        customerId
                );

        System.out.println();

        System.out.println(
                "Total Spent: " +
                        totalSpent +
                        " EGP"
        );
    }

    // =========================================================
    // REPORT 9
    // PEAK ORDERING HOUR
    // =========================================================

    private void viewPeakOrderingHour() {

        System.out.println();
        System.out.println(
                "===== Peak Ordering Hour ====="
        );

        Optional<Integer> result =
                system.getPeakOrderingHour();

        if (result.isEmpty()) {

            System.out.println(
                    "No orders found."
            );

            return;
        }

        int hour =
                result.get();

        System.out.println(
                "Peak Ordering Hour: " +
                        String.format("%02d:00", hour)
        );

        System.out.println(
                "Time Range: " +
                        String.format(
                                "%02d:00 - %02d:00",
                                hour,
                                hour + 1
                        )
        );
    }

    // =========================================================
    // REPORT 10
    // CUSTOMERS WITH NO RECENT ORDERS
    // =========================================================

    private void viewCustomersWithNoRecentOrders() {

        System.out.println();
        System.out.println(
                "===== Customers With No Recent Orders ====="
        );

        LocalDateTime now =
                LocalDateTime.now();

        List<Customer> customers =
                system.getCustomersWithNoRecentOrders(
                        now
                );

        if (customers.isEmpty()) {

            System.out.println(
                    "All customers have ordered within the last 30 days."
            );

            return;
        }

        System.out.println();

        System.out.println(
                "Customers who have not ordered in the last 30 days:"
        );

        customers.forEach(customer -> {

            System.out.println();

            System.out.println(
                    "Customer ID: " +
                            customer.getId()
            );

            System.out.println(
                    "Name: " +
                            customer.getName()
            );

            System.out.println(
                    "Mobile: " +
                            customer.getMobile()
            );

            System.out.println(
                    "-------------------------"
            );
        });
    }

    // =========================================================
    // PLATFORM STATISTICS
    // =========================================================

    private void viewPlatformStatistics() {

        System.out.println();
        System.out.println(
                "===== Platform Statistics ====="
        );

        int restaurants =
                system.getRestaurantsCount();

        int customers =
                system.getCustomersCount();

        int riders =
                system.getRidersCount();

        int totalOrders =
                system.getTotalOrdersCount();

        long deliveredOrders =
                system.getDeliveredOrdersCount();

        long cancelledOrders =
                system.getCancelledOrdersCount();

        double revenue =
                system.getPlatformRevenue();

        System.out.println();

        System.out.println(
                "Restaurants: " +
                        restaurants
        );

        System.out.println(
                "Customers: " +
                        customers
        );

        System.out.println(
                "Riders: " +
                        riders
        );

        System.out.println(
                "Total Orders: " +
                        totalOrders
        );

        System.out.println(
                "Delivered Orders: " +
                        deliveredOrders
        );

        System.out.println(
                "Cancelled Orders: " +
                        cancelledOrders
        );

        System.out.println(
                "Platform Revenue: " +
                        revenue +
                        " EGP"
        );
    }
}