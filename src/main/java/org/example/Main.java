package org.example;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        DeliveryFeeCalculator deliveryFeeCalculator =
                new DeliveryFeeCalculator();

        ServiceFeeCalculator serviceFeeCalculator =
                new ServiceFeeCalculator();

        PromotionContext promotionContext =
                new PromotionContext();

        OrderPricingService pricingService =
                new OrderPricingService(
                        deliveryFeeCalculator,
                        serviceFeeCalculator,
                        promotionContext
                );

        RestaurantSystem system =
                new RestaurantSystem(pricingService);

        boolean running = true;

        while (running) {

            printMainMenu();

            int choice =
                    readInt(
                            scanner,
                            "Choose an option: "
                    );

            try {

                switch (choice) {

                    case 1:
                        customerArea(
                                system,
                                scanner
                        );
                        break;

                    case 2:
                        restaurantArea(
                                system,
                                scanner
                        );
                        break;

                    case 3:
                        riderArea(
                                system,
                                scanner
                        );
                        break;

                    case 4:
                        adminArea(
                                system,
                                scanner
                        );
                        break;

                    case 0:

                        running = false;

                        System.out.println();
                        System.out.println(
                                "Thank you for using MASR DELIVERY."
                        );

                        break;

                    default:

                        System.out.println(
                                "Invalid option."
                        );
                }

            } catch (PlatformException e) {

                System.out.println();
                System.out.println(
                        "Error: " +
                                e.getMessage()
                );

            } catch (IllegalArgumentException e) {

                System.out.println();
                System.out.println(
                        "Error: " +
                                e.getMessage()
                );

            } catch (IllegalStateException e) {

                System.out.println();
                System.out.println(
                        "Error: " +
                                e.getMessage()
                );
            }
        }

        scanner.close();
    }

    // =========================================================
    // MAIN MENU
    // =========================================================

    private static void printMainMenu() {

        System.out.println();
        System.out.println(
                "============================================"
        );

        System.out.println(
                "          MASR DELIVERY — Main Menu"
        );

        System.out.println(
                "============================================"
        );

        System.out.println(
                "1. Customer"
        );

        System.out.println(
                "2. Restaurant"
        );

        System.out.println(
                "3. Rider"
        );

        System.out.println(
                "4. Admin & Reports"
        );

        System.out.println(
                "0. Exit"
        );

        System.out.println(
                "============================================"
        );
    }

    // =========================================================
    // CUSTOMER AREA
    // =========================================================

    private static void customerArea(
            RestaurantSystem system,
            Scanner scanner
    ) {

        System.out.println();
        System.out.println(
                "===== Customer Area ====="
        );
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        
        int choice = readInt(scanner, "Choose an option: ");
        
        if (choice == 2) {
            System.out.print("Enter New Customer ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Mobile: ");
            String mobile = scanner.nextLine();
            System.out.print("Enter Initial Balance (EGP): ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            
            Customer newCustomer = new Customer(id, name, mobile, balance);
            system.addCustomer(newCustomer);
            System.out.println("Customer created successfully. You can now log in.");
            return;
        } else if (choice != 1) {
            System.out.println("Invalid option.");
            return;
        }

        System.out.print(
                "Customer ID: "
        );

        int customerId =
                scanner.nextInt();

        scanner.nextLine();

        Customer customer =
                system.getCustomer(customerId);

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return;
        }

        CustomerConsole customerConsole =
                new CustomerConsole(
                        scanner,
                        system,
                        customer
                );

        customerConsole.showMenu();
    }

    // =========================================================
    // RESTAURANT AREA
    // =========================================================

    private static void restaurantArea(
            RestaurantSystem system,
            Scanner scanner
    ) {

        System.out.println();
        System.out.println(
                "===== Restaurant Area ====="
        );
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        
        int rChoice = readInt(scanner, "Choose an option: ");
        
        if (rChoice == 2) {
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
            Restaurant restaurant = new Restaurant(id, name, district, rating, cuisine);
            system.addRestaurant(restaurant);
            System.out.println("Restaurant created successfully. You can now log in.");
            return;
        } else if (rChoice != 1) {
            System.out.println("Invalid option.");
            return;
        }

        System.out.print(
                "Restaurant ID: "
        );

        int restaurantId =
                scanner.nextInt();

        scanner.nextLine();

        Restaurant restaurant =
                system.getRestaurant(
                        restaurantId
                );

        if (restaurant == null) {

            System.out.println(
                    "Restaurant not found."
            );

            return;
        }

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println(
                    "===== " +
                            restaurant.getName() +
                            " ====="
            );

            System.out.println(
                    "1. Accept Order"
            );

            System.out.println(
                    "2. Reject Order"
            );

            System.out.println(
                    "3. Mark Order Preparing"
            );

            System.out.println(
                    "4. Mark Order Ready"
            );

            System.out.println(
                    "5. Toggle Item Availability"
            );

            System.out.println(
                    "6. Add Menu Item"
            );

            System.out.println(
                    "7. Remove Menu Item"
            );

            System.out.println(
                    "8. Adjust Daily Stock"
            );

            System.out.println(
                    "9. View Today's Orders"
            );

            System.out.println(
                    "10. View Today's Revenue"
            );

            System.out.println(
                    "0. Back"
            );

            int choice =
                    readInt(
                            scanner,
                            "Choose an option: "
                    );

            try {

                switch (choice) {

                    case 1:
                        acceptOrder(
                                system,
                                scanner
                        );
                        break;

                    case 2:
                        rejectOrder(
                                system,
                                scanner
                        );
                        break;

                    case 3:
                        startPreparing(
                                system,
                                scanner
                        );
                        break;

                    case 4:
                        markReady(
                                system,
                                scanner
                        );
                        break;

                    case 5:
                        toggleItemAvailability(
                                system,
                                scanner,
                                restaurant
                        );
                        break;

                    case 6:
                        addMenuItem(
                                system,
                                scanner,
                                restaurant
                        );
                        break;

                    case 7:
                        removeMenuItem(
                                system,
                                scanner,
                                restaurant
                        );
                        break;

                    case 8:
                        adjustDailyStock(
                                system,
                                scanner,
                                restaurant
                        );
                        break;

                    case 9:
                        viewTodaysOrders(
                                system,
                                restaurant
                        );
                        break;

                    case 10:
                        viewTodaysRevenue(
                                system,
                                restaurant
                        );
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
                        "Error: " +
                                e.getMessage()
                );

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Error: " +
                                e.getMessage()
                );

            } catch (IllegalStateException e) {

                System.out.println(
                        "Error: " +
                                e.getMessage()
                );
            }
        }
    }

    // =========================================================
    // ACCEPT ORDER
    // =========================================================

    private static void acceptOrder(
            RestaurantSystem system,
            Scanner scanner
    ) {

        System.out.print(
                "Order ID: "
        );

        int orderId =
                scanner.nextInt();

        scanner.nextLine();

        system.acceptOrder(orderId);

        System.out.println(
                "Order accepted successfully."
        );
    }

    // =========================================================
    // REJECT ORDER
    // =========================================================

    private static void rejectOrder(
            RestaurantSystem system,
            Scanner scanner
    ) {

        System.out.print(
                "Order ID: "
        );

        int orderId =
                scanner.nextInt();

        scanner.nextLine();

        system.rejectOrder(orderId);

        System.out.println(
                "Order rejected successfully."
        );
    }

    // =========================================================
    // START PREPARING
    // =========================================================

    private static void startPreparing(
            RestaurantSystem system,
            Scanner scanner
    ) {

        System.out.print(
                "Order ID: "
        );

        int orderId =
                scanner.nextInt();

        scanner.nextLine();

        system.startPreparingOrder(
                orderId
        );

        System.out.println(
                "Order is now PREPARING."
        );
    }

    // =========================================================
    // MARK READY
    // =========================================================

    private static void markReady(
            RestaurantSystem system,
            Scanner scanner
    ) {

        System.out.print(
                "Order ID: "
        );

        int orderId =
                scanner.nextInt();

        scanner.nextLine();

        system.markOrderReady(
                orderId
        );

        System.out.println(
                "Order is now READY."
        );
    }

    // =========================================================
    // TOGGLE ITEM AVAILABILITY
    // =========================================================

    private static void toggleItemAvailability(
            RestaurantSystem system,
            Scanner scanner,
            Restaurant restaurant
    ) {

        List<MenuItem> menu =
                restaurant.getMenu();

        if (menu.isEmpty()) {

            System.out.println(
                    "No menu items found."
            );

            return;
        }

        System.out.println();

        for (int i = 0; i < menu.size(); i++) {

            MenuItem item =
                    menu.get(i);

            System.out.println(
                    (i + 1) +
                            ". " +
                            item.getName() +
                            " - " +
                            item.getPrice() +
                            " EGP" +
                            " [" +
                            (item.isAvailable()
                                    ? "AVAILABLE"
                                    : "UNAVAILABLE") +
                            "]"
            );
        }

        System.out.print(
                "Choose item number: "
        );

        int itemNumber =
                scanner.nextInt();

        scanner.nextLine();

        system.toggleItemAvailability(
                restaurant,
                itemNumber - 1
        );

        System.out.println(
                "Item availability updated."
        );
    }
    
    // =========================================================
    // ADD MENU ITEM
    // =========================================================

    private static void addMenuItem(
            RestaurantSystem system,
            Scanner scanner,
            Restaurant restaurant
    ) {
        System.out.println();
        System.out.println("===== Add Menu Item =====");
        System.out.print("Item ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Item Name: ");
        String name = scanner.nextLine();
        System.out.print("Type (STANDARD, WEIGHTED, COMBO): ");
        String type = scanner.nextLine();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Preparation Time (minutes): ");
        int prepTime = scanner.nextInt();
        scanner.nextLine();

        MenuItem item = MenuItemFactory.create(type, id, name, price, category, prepTime, true, new java.util.ArrayList<>(), 0.0);
        system.addMenuItem(restaurant, item);
        System.out.println("Menu item added successfully.");
    }

    // =========================================================
    // REMOVE MENU ITEM
    // =========================================================

    private static void removeMenuItem(
            RestaurantSystem system,
            Scanner scanner,
            Restaurant restaurant
    ) {
        List<MenuItem> menu = restaurant.getMenu();
        if (menu.isEmpty()) {
            System.out.println("No menu items found.");
            return;
        }

        System.out.println();
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).getName());
        }

        System.out.print("Choose item number to remove: ");
        int itemNumber = scanner.nextInt();
        scanner.nextLine();

        system.removeMenuItem(restaurant, itemNumber - 1);
        System.out.println("Menu item removed successfully.");
    }

    // =========================================================
    // ADJUST STOCK
    // =========================================================

    private static void adjustDailyStock(
            RestaurantSystem system,
            Scanner scanner,
            Restaurant restaurant
    ) {

        List<MenuItem> menu =
                restaurant.getMenu();

        if (menu.isEmpty()) {

            System.out.println(
                    "No menu items found."
            );

            return;
        }

        System.out.println();

        for (int i = 0; i < menu.size(); i++) {

            MenuItem item =
                    menu.get(i);

            System.out.println(
                    (i + 1) +
                            ". " +
                            item.getName() +
                            " | Stock: " +
                            item.getDailyStock()
            );
        }

        System.out.print(
                "Choose item number: "
        );

        int itemNumber =
                scanner.nextInt();

        System.out.print(
                "Stock adjustment (+/-): "
        );

        int amount =
                scanner.nextInt();

        scanner.nextLine();

        system.adjustDailyStock(
                restaurant,
                itemNumber - 1,
                amount
        );

        System.out.println(
                "Daily stock updated."
        );
    }

    // =========================================================
    // TODAY'S ORDERS
    // =========================================================

    private static void viewTodaysOrders(
            RestaurantSystem system,
            Restaurant restaurant
    ) {

        System.out.println();
        System.out.println(
                "===== Today's Orders ====="
        );

        List<Order> orders =
                system.getTodaysOrders(
                        restaurant.getId()
                );

        if (orders.isEmpty()) {

            System.out.println(
                    "No orders found today."
            );

            return;
        }

        for (Order order : orders) {

            System.out.println();

            System.out.println(
                    "Order ID: " +
                            order.getId()
            );

            System.out.println(
                    "Customer: " +
                            order.getCustomer()
                                    .getName()
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

            System.out.println(
                    "Placed At: " +
                            order.getPlacedAt()
            );

            System.out.println(
                    "-------------------------"
            );
        }
    }

    // =========================================================
    // TODAY'S REVENUE
    // =========================================================

    private static void viewTodaysRevenue(
            RestaurantSystem system,
            Restaurant restaurant
    ) {

        System.out.println();
        System.out.println(
                "===== Today's Revenue ====="
        );

        List<Order> orders =
                system.getTodaysOrders(
                        restaurant.getId()
                );

        double revenue =
                orders.stream()
                        .filter(order ->
                                order.getStatus()
                                        == OrderStatus.DELIVERED
                        )
                        .mapToDouble(
                                Order::getTotal
                        )
                        .sum();

        System.out.println(
                "Today's Revenue: " +
                        revenue +
                        " EGP"
        );
    }

    // =========================================================
    // RIDER AREA
    // =========================================================

    private static void riderArea(
            RestaurantSystem system,
            Scanner scanner
    ) {

        System.out.println();
        System.out.println(
                "===== Rider Area ====="
        );
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        
        int rChoice = readInt(scanner, "Choose an option: ");
        
        if (rChoice == 2) {
            System.out.print("Enter New Rider ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Vehicle Type (1: MOTORCYCLE, 2: BICYCLE, 3: CAR): ");
            int type = scanner.nextInt();
            scanner.nextLine();
            VehicleType vehicleType = type == 1 ? VehicleType.MOTORCYCLE : type == 2 ? VehicleType.BICYCLE : VehicleType.CAR;
            DispatchStrategy ds = type == 1 ? new MotorcycleDispatchStrategy() : type == 2 ? new BicycleDispatchStrategy() : new CarDispatchStrategy();
            System.out.print("Enter District: ");
            String district = scanner.nextLine();
            
            Rider newRider = new Rider(id, name, vehicleType, district, ds);
            system.addRider(newRider);
            System.out.println("Rider created successfully. You can now log in.");
            return;
        } else if (rChoice != 1) {
            System.out.println("Invalid option.");
            return;
        }

        System.out.print(
                "Rider ID: "
        );

        int riderId =
                scanner.nextInt();

        scanner.nextLine();

        Rider rider =
                getRider(
                        system,
                        riderId
                );

        if (rider == null) {

            System.out.println(
                    "Rider not found."
            );

            return;
        }

        RiderConsole riderConsole =
                new RiderConsole(
                        system,
                        scanner,
                        rider
                );

        riderConsole.start();
    }

    // =========================================================
    // GET RIDER
    // =========================================================

    private static Rider getRider(
            RestaurantSystem system,
            int riderId
    ) {

        // We need a getter in RestaurantSystem.
        return system.getRider(riderId);
    }

    // =========================================================
    // ADMIN AREA
    // =========================================================

    private static void adminArea(
            RestaurantSystem system,
            Scanner scanner
    ) {

        AdminConsole adminConsole =
                new AdminConsole(
                        system,
                        scanner
                );

        adminConsole.start();
    }

    // =========================================================
    // INPUT HELPER
    // =========================================================

    private static int readInt(
            Scanner scanner,
            String message
    ) {

        while (true) {

            System.out.print(message);

            if (scanner.hasNextInt()) {

                int value =
                        scanner.nextInt();

                scanner.nextLine();

                return value;
            }

            System.out.println(
                    "Please enter a valid number."
            );

            scanner.nextLine();
        }
    }
}