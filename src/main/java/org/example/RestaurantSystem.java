package org.example;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RestaurantSystem {
    private int nextOrderId = 1;

    private final Map<Integer, Restaurant> restaurantsById;
    private final Map<Integer, Customer> customersById;
    private final Map<Integer, Rider> ridersById;
    private final Map<Integer, Order> ordersById;

    private final List<MenuItem> menuItems;

    private final Set<String> cuisineCategories;

    private final PriorityQueue<Order> readyOrders;
    private final Deque<String> recentSearches;
    private final List<Restaurant> RestaurantRevenue;
    private final OrderPricingService orderPricingService;

    public RestaurantSystem(OrderPricingService orderPricingService) {
        this.orderPricingService = orderPricingService;
        restaurantsById = new HashMap<>();
        customersById = new HashMap<>();
        ridersById = new HashMap<>();
        ordersById = new HashMap<>();

        menuItems = new ArrayList<>();
        cuisineCategories = new HashSet<>();

        readyOrders = new PriorityQueue<>((o1, o2) -> {

            LoyaltyTier tier1 =
                    o1.getCustomer().getLoyaltyTier();

            LoyaltyTier tier2 =
                    o2.getCustomer().getLoyaltyTier();

            if (tier1 != tier2) {

                if (tier1 == LoyaltyTier.GOLD) {
                    return -1;
                }

                if (tier2 == LoyaltyTier.GOLD) {
                    return 1;
                }
            }

            return o1.getPlacedAt()
                    .compareTo(o2.getPlacedAt());
        });

        recentSearches = new ArrayDeque<>();

        RestaurantRevenue = new ArrayList<>();

    }

    // =========================
    // RESTAURANT FUNCTIONS
    // =========================

    public List<Restaurant> getRestaurantsSortedByRating() {

        List<Restaurant> sortedRestaurants =
                new ArrayList<>(restaurantsById.values());

        sortedRestaurants.sort(
                Comparator
                        .comparingDouble(Restaurant::getRating)
                        .reversed()
                        .thenComparing(Restaurant::getName)
        );

        return sortedRestaurants;
    }

    // =========================
    // SEARCH HISTORY
    // =========================

    public void addSearch(String search) {

        recentSearches.addFirst(search);

        if (recentSearches.size() > 5) {
            recentSearches.removeLast();
        }
    }

    public List<String> getRecentSearches() {
        return List.copyOf(recentSearches);
    }

    // =========================
    // MENU
    // =========================

    public List<MenuItem> getMenuItems() {
        return List.copyOf(menuItems);
    }

    // =========================
    // REPORTS
    // =========================

    public double getTotalRevenue(
            LocalDateTime from,
            LocalDateTime to
    ) {

        return ordersById.values()
                .stream()

                .filter(order ->
                        !order.getPlacedAt().isBefore(from)
                                && !order.getPlacedAt().isAfter(to)
                )

                .filter(order ->
                        order.getStatus() == OrderStatus.DELIVERED
                )

                .mapToDouble(Order::getTotal)
                .sum();
    }

    public Map<Restaurant, Double> getTopFiveRestaurantsByRevenue(
            YearMonth month
    ) {

        return ordersById.values()
                .stream()

                .filter(order ->
                        order.getStatus() == OrderStatus.DELIVERED
                )

                .filter(order ->
                        YearMonth.from(order.getPlacedAt())
                                .equals(month)
                )

                .collect(Collectors.groupingBy(
                        Order::getRestaurant,
                        Collectors.summingDouble(Order::getTotal)
                ))

                .entrySet()
                .stream()

                .sorted(
                        Map.Entry.<Restaurant, Double>
                                        comparingByValue()
                                .reversed()
                )

                .limit(5)

                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Double> getAverageOrderValuePerDistrict() {

        return ordersById.values()
                .stream()

                .collect(Collectors.groupingBy(
                        order ->
                                order.getDeliveryAddress()
                                        .getDistrict(),

                        Collectors.averagingDouble(
                                Order::getTotal
                        )
                ));
    }

    public List<Restaurant> getHighlyRatedRestaurants() {

        return restaurantsById.values()
                .stream()

                .filter(r ->
                        r.getRating() > 4.5
                )

                .filter(r ->
                        ordersById.values()
                                .stream()

                                .filter(order ->
                                        order.getRestaurant()
                                                .equals(r)
                                )

                                .filter(order ->
                                        order.getStatus()
                                                == OrderStatus.DELIVERED
                                )

                                .count() >= 20
                )

                .toList();
    }

    public Map<OrderStatus, Long> getOrdersCountByStatus() {

        return ordersById.values()
                .stream()

                .collect(Collectors.groupingBy(
                        Order::getStatus,
                        Collectors.counting()
                ));
    }

    public List<Rider> getRiderDeliveryReport() {

        return ridersById.values()
                .stream()

                .sorted(
                        Comparator
                                .comparing(
                                        Rider::getCompletedDeliveries
                                )
                                .reversed()
                )

                .toList();
    }

    public Optional<MenuItem> getMostFrequentlyOrderedItem() {

        return ordersById.values()
                .stream()

                .flatMap(order ->
                        order.getItems().stream()
                )

                .collect(Collectors.groupingBy(
                        Order_item::getMenuItem,
                        Collectors.summingDouble(
                                Order_item::getQuantity
                        )
                ))

                .entrySet()
                .stream()

                .max(Map.Entry.comparingByValue())

                .map(Map.Entry::getKey);
    }


    public Optional<Integer> getPeakOrderingHour() {

        return ordersById.values()
                .stream()

                .collect(Collectors.groupingBy(
                        order ->
                                order.getPlacedAt().getHour(),

                        Collectors.counting()
                ))

                .entrySet()
                .stream()

                .max(Map.Entry.comparingByValue())

                .map(Map.Entry::getKey);
    }

    public List<Customer> getCustomersWithNoRecentOrders(
            LocalDateTime now
    ) {

        LocalDateTime thirtyDaysAgo =
                now.minusDays(30);

        return customersById.values()
                .stream()

                .filter(customer ->
                        ordersById.values()
                                .stream()

                                .filter(order ->
                                        order.getCustomer()
                                                .equals(customer)
                                )

                                .noneMatch(order ->
                                        !order.getPlacedAt()
                                                .isBefore(thirtyDaysAgo)

                                                &&

                                                !order.getPlacedAt()
                                                        .isAfter(now)
                                )
                )

                .toList();
    }

    // =========================
    // CUSTOMER FUNCTIONS
    // =========================

    public List<Restaurant> browseRestaurants(
            String district,
            String cuisine,
            Double minimumRating
    ) {

        return restaurantsById.values()
                .stream()

                // Open restaurants only
                .filter(Restaurant::isOpen)

                // District filter
                .filter(r ->
                        district.isBlank()
                                || r.getDistrict()
                                .equalsIgnoreCase(district)
                )

                // Cuisine filter
                .filter(r ->
                        cuisine.isBlank()
                                || r.getCuisine()
                                .equalsIgnoreCase(cuisine)
                )

                // Minimum rating filter
                .filter(r ->
                        minimumRating == null
                                || r.getRating() >= minimumRating
                )

                // Highest rating first
                .sorted(
                        Comparator
                                .comparingDouble(
                                        Restaurant::getRating
                                )
                                .reversed()
                )

                .toList();
    }


    public List<Restaurant> searchRestaurants(String text) {

        return restaurantsById.values()
                .stream()
                .filter(r ->
                        r.getName().toLowerCase().contains(text.toLowerCase())
                                || r.getCuisine().toLowerCase().contains(text.toLowerCase())
                )
                .toList();
    }
    public  Restaurant getRestaurant(int id){
        return restaurantsById.get(id);
    }


    public Order placeOrder(
            Customer customer,
            Restaurant restaurant,
            List<Order_item> items,
            Address address,
            String promoCode,
            double distanceKm) {



        if (customer == null) {
            throw new PlatformException(
                    "Customer not found."
            );
        }

        if (restaurant == null) {
            throw new PlatformException(
                    "Restaurant not found."
            );
        }

        if (items == null || items.isEmpty()) {
            throw new PlatformException(
                    "Order must contain at least one item."
            );
        }

        if (address == null) {
            throw new PlatformException(
                    "Delivery address is required."
            );
        }

        if (distanceKm < 0) {
            throw new PlatformException(
                    "Distance cannot be negative."
            );
        }


        // =========================
        // CALCULATE SUBTOTAL
        // =========================

        double subtotal = 0;

        for (Order_item item : items) {

            if (item == null) {
                throw new PlatformException(
                        "Invalid order item."
                );
            }

            MenuItem menuItem =
                    item.getMenuItem();

            if (menuItem == null) {
                throw new PlatformException(
                        "Invalid menu item."
                );
            }

            if (!menuItem.isAvailable()) {
                throw new PlatformException(
                        menuItem.getName()
                                + " is unavailable."
                );
            }

            if (item.getQuantity() <= 0) {
                throw new PlatformException(
                        "Quantity must be greater than zero."
                );
            }

            subtotal +=
                    menuItem.getPrice()
                            * item.getQuantity();
        }


        // =========================
        // DELIVERY FEE
        // =========================

        double deliveryFee =
                orderPricingService
                        .calculateDelivery(
                                distanceKm
                        );


        // =========================
        // SERVICE FEE
        // =========================

        double serviceFee =
                orderPricingService
                        .calculateService(
                                subtotal
                        );


        // =========================
        // PROMOTION
        // =========================

        PromotionResult promotion =
                orderPricingService.applyPromotion(
                        subtotal,
                        deliveryFee
                );

        if (promotion.isFreeDelivery()) {
            deliveryFee = 0;
        }

        double discount =
                promotion.getDiscount();


        // =========================
        // TOTAL
        // =========================

        double total =
                subtotal
                        + deliveryFee
                        + serviceFee
                        - discount;


        // =========================
        // CREATE ORDER
        // =========================

        Order.Builder builder =
                new Order.Builder()
                        .id(generateOrderId())
                        .customer(customer)
                        .restaurant(restaurant)
                        .deliveryAddress(address)
                        .subtotal(subtotal)
                        .deliveryFee(deliveryFee)
                        .serviceFee(serviceFee)
                        .discount(discount)
                        .total(total);


        // Add order items

        for (Order_item item : items) {
            builder.addItem(item);
        }




        Order order = builder.build();

        ordersById.put(
                order.getId(),
                order
        );

        return order;
    }
    private int generateOrderId() {
        return nextOrderId++;
    }
    public Order getOrder(int orderId) {

        return ordersById.get(orderId);
    }
    public void payFromWallet(
            Customer customer,
            Order order) {

        if (customer == null) {
            throw new PlatformException(
                    "Customer not found."
            );
        }

        if (order == null) {
            throw new PlatformException(
                    "Order not found."
            );
        }

        double amount = order.getTotal();

        if (customer.getBalance() < amount) {

            throw new PlatformException(
                    "Insufficient funds. "
                            + "Required: "
                            + amount
                            + " EGP, Available: "
                            + customer.getBalance()
                            + " EGP."
            );
        }

        // Deduct from customer balance
        customer.deductBalance(amount);

        System.out.println();
        System.out.println(
                "========== PAYMENT CONFIRMED =========="
        );

        System.out.println(
                "Order Reference: "
                        + order.getId()
        );

        System.out.println(
                "Amount Paid: "
                        + amount
                        + " EGP"
        );

        System.out.println(
                "New Balance: "
                        + customer.getBalance()
                        + " EGP"
        );

        System.out.println(
                "========================================"
        );
    }
    public void addCustomer(Customer customer) {
        if (customer != null) {
            customersById.put(customer.getId(), customer);
        }
    }

    public Customer getCustomer(int customerId) {
        return customersById.get(customerId);
    }
    public void cancelOrder(
            int customerId,
            int orderId) {

        Customer customer =
                getCustomer(customerId);

        if (customer == null) {
            throw new PlatformException(
                    "Customer not found."
            );
        }

        Order order =
                getOrder(orderId);

        if (order == null) {
            throw new PlatformException(
                    "Order not found."
            );
        }

        if (order.getCustomer().getId()
                != customerId) {

            throw new PlatformException(
                    "This order does not belong to the customer."
            );
        }

        double refundAmount =
                order.getTotal();

        try {

            // Verify legal transition
            order.cancel();

        } catch (IllegalStateException e) {

            throw new PlatformException(
                    e.getMessage()
            );
        }


        customer.refundBalance(refundAmount);
    }
    public List<Order> getCustomerOrderHistory(int customerId) {

        List<Order> history = new ArrayList<>();

        for (Order order : ordersById.values()) {

            if (order.getCustomer().getId() == customerId) {
                history.add(order);
            }
        }

        history.sort(
                Comparator.comparing(Order::getPlacedAt)
                        .reversed()
        );

        return history;
    }
    public double getCustomerLifetimeSpent(int customerId) {

        double totalSpent = 0;

        for (Order order : ordersById.values()) {

            if (order.getCustomer().getId() == customerId) {
                totalSpent += order.getTotal();
            }
        }

        return totalSpent;
    }
    public void acceptOrder(int orderId) {

        Order order = getOrder(orderId);

        if (order == null) {
            throw new PlatformException(
                    "Order not found."
            );
        }

        try {
            order.accept();
        } catch (IllegalStateException e) {
            throw new PlatformException(
                    e.getMessage()
            );
        }
    }
    public void rejectOrder(int orderId) {

        Order order = getOrder(orderId);

        if (order == null) {
            throw new PlatformException(
                    "Order not found."
            );
        }

        try {

            order.reject();

        } catch (IllegalStateException e) {

            throw new PlatformException(
                    e.getMessage()
            );
        }
    }
    public void startPreparingOrder(int orderId) {

        Order order = getOrder(orderId);

        if (order == null) {
            throw new PlatformException(
                    "Order not found."
            );
        }

        try {
            order.startPreparing();
        } catch (IllegalStateException e) {
            throw new PlatformException(
                    e.getMessage()
            );
        }
    }
    public void markOrderReady(int orderId) {

        Order order = getOrder(orderId);

        if (order == null) {
            throw new PlatformException(
                    "Order not found."
            );
        }

        try {
            order.markReady();
            readyOrders.add(order);
            dispatchOrders();
        } catch (IllegalStateException e) {
            throw new PlatformException(
                    e.getMessage()
            );
        }
    }
    public void toggleItemAvailability(
            Restaurant restaurant,
            int itemIndex) {

        List<MenuItem> menu =
                restaurant.getMenu();

        if (itemIndex < 0 ||
                itemIndex >= menu.size()) {

            throw new PlatformException(
                    "Menu item not found."
            );
        }

        MenuItem item =
                menu.get(itemIndex);

        item.setAvailable(
                !item.isAvailable()
        );
    }
    public void addMenuItem(
            Restaurant restaurant,
            MenuItem item) {

        if (restaurant == null) {
            throw new PlatformException(
                    "Restaurant not found."
            );
        }

        if (item == null) {
            throw new PlatformException(
                    "Menu item cannot be null."
            );
        }

        restaurant.addMenuItem(item);
    }
    public void removeMenuItem(
            Restaurant restaurant,
            int itemIndex) {

        List<MenuItem> menu =
                restaurant.getMenu();

        if (itemIndex < 0 ||
                itemIndex >= menu.size()) {

            throw new PlatformException(
                    "Menu item not found."
            );
        }

        menu.remove(itemIndex);
    }
    public void adjustDailyStock(
            Restaurant restaurant,
            int itemIndex,
            int amount) {

        MenuItem item =
                restaurant.getMenu().get(itemIndex);

        int newStock =
                item.getDailyStock() + amount;

        if (newStock < 0) {
            throw new PlatformException(
                    "Stock cannot become negative."
            );
        }

        item.setDailyStock(newStock);
    }
    public List<Order> getTodaysOrders(int restaurantId) {

        LocalDateTime startOfDay =
                LocalDateTime.now()
                        .toLocalDate()
                        .atStartOfDay();

        LocalDateTime endOfDay =
                startOfDay.plusDays(1);

        return ordersById.values()
                .stream()

                .filter(order ->
                        order.getRestaurant().getId()
                                == restaurantId
                )

                .filter(order ->
                        !order.getPlacedAt().isBefore(startOfDay)
                                &&
                                order.getPlacedAt().isBefore(endOfDay)
                )

                .sorted(
                        Comparator.comparing(
                                Order::getPlacedAt
                        )
                )

                .collect(Collectors.toList());
    }
    public void goOnDuty(int riderId) {

        Rider rider = ridersById.get(riderId);

        if (rider == null) {
            throw new PlatformException("Rider not found.");
        }

        rider.goOnDuty();
        dispatchOrders();
    }
    public void goOffDuty(int riderId) {

        Rider rider = ridersById.get(riderId);

        if (rider == null) {
            throw new PlatformException("Rider not found.");
        }

        if (rider.hasActiveOrder()) {
            throw new PlatformException(
                    "Rider cannot go off duty while having an active order."
            );
        }

        rider.goOffDuty();
    }
    public Order getRiderActiveOrder(int riderId) {

        Rider rider = ridersById.get(riderId);

        if (rider == null) {
            throw new PlatformException("Rider not found.");
        }

        return rider.getActiveOrder();
    }
    public void assignOrderToRider(int orderId, int riderId) {

        Order order = ordersById.get(orderId);
        Rider rider = ridersById.get(riderId);

        if (order == null) {
            throw new PlatformException("Order not found.");
        }

        if (rider == null) {
            throw new PlatformException("Rider not found.");
        }

        if (rider.getAvailabilityStatus()
                != AvailabilityStatus.AVAILABLE) {

            throw new PlatformException(
                    "Rider is not available."
            );
        }

        if (rider.hasActiveOrder()) {

            throw new PlatformException(
                    "Rider already has an active order."
            );
        }

        if (order.getStatus() != OrderStatus.READY) {

            throw new PlatformException(
                    "Order must be READY before assigning a rider."
            );
        }

        rider.assignOrder(order);

        order.assignRider();
    }

    private void dispatchOrders() {
        List<Order> unassignedOrders = new ArrayList<>();

        while (!readyOrders.isEmpty()) {
            Order order = readyOrders.poll();

            Optional<Rider> selectedRider = ridersById.values().stream()
                    .filter(r -> r.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE)
                    .filter(r -> !r.hasActiveOrder())
                    .filter(r -> r.canHandleOrder(order))
                    .findFirst();

            if (selectedRider.isPresent()) {
                try {
                    assignOrderToRider(order.getId(), selectedRider.get().getId());
                } catch (Exception e) {
                    unassignedOrders.add(order);
                }
            } else {
                unassignedOrders.add(order);
            }
        }

        readyOrders.addAll(unassignedOrders);
    }
    public void markOrderPickedUp(int riderId) {

        Rider rider = ridersById.get(riderId);

        if (rider == null) {
            throw new PlatformException("Rider not found.");
        }

        Order order = rider.getActiveOrder();

        if (order == null) {
            throw new PlatformException(
                    "Rider has no active order."
            );
        }

        try {

            rider.pickUpOrder();

            order.startDelivery();

        } catch (IllegalStateException e) {

            throw new PlatformException(
                    e.getMessage()
            );
        }
    }
    public void markOrderDelivered(int riderId) {

        Rider rider = ridersById.get(riderId);

        if (rider == null) {
            throw new PlatformException("Rider not found.");
        }

        Order order = rider.getActiveOrder();

        if (order == null) {
            throw new PlatformException(
                    "Rider has no active order."
            );
        }

        try {

            order.deliver();

            LocalDateTime started =
                    rider.getDeliveryStartedAt();

            long deliveryMinutes = 0;

            if (started != null) {

                deliveryMinutes =
                        java.time.Duration
                                .between(
                                        started,
                                        LocalDateTime.now()
                                )
                                .toMinutes();
            }

            rider.completeCurrentOrder(
                    deliveryMinutes
            );
            
            dispatchOrders();

        } catch (IllegalStateException e) {

            throw new PlatformException(
                    e.getMessage()
            );
        }
    }
    public void addRestaurant(Restaurant restaurant) {

        if (restaurant == null) {
            throw new PlatformException(
                    "Restaurant cannot be null."
            );
        }

        if (restaurantsById.containsKey(restaurant.getId())) {
            throw new PlatformException(
                    "Restaurant with this ID already exists."
            );
        }

        restaurantsById.put(
                restaurant.getId(),
                restaurant
        );
    }
    public void removeRestaurant(int restaurantId) {

        Restaurant restaurant =
                restaurantsById.get(restaurantId);

        if (restaurant == null) {
            throw new PlatformException(
                    "Restaurant not found."
            );
        }

        restaurantsById.remove(restaurantId);
    }
    public void createPercentagePromotion(
            double percentage,
            double maxDiscount
    ) {
        orderPricingService.setPromotionStrategy(
                new PercentagePromotion(
                        percentage,
                        maxDiscount
                )
        );
    }

    public void createFixedAmountPromotion(
            double amount
    ) {
        orderPricingService.setPromotionStrategy(
                new FixedAmountPromotion(amount)
        );
    }

    public void createFreeDeliveryPromotion() {
        orderPricingService.setPromotionStrategy(
                new FreeDeliveryPromotion()
        );
    }
    public int getRestaurantsCount() {
        return restaurantsById.size();
    }

    public int getCustomersCount() {
        return customersById.size();
    }

    public int getRidersCount() {
        return ridersById.size();
    }

    public int getTotalOrdersCount() {
        return ordersById.size();
    }

    public long getDeliveredOrdersCount() {

        return ordersById.values()
                .stream()
                .filter(order ->
                        order.getStatus()
                                == OrderStatus.DELIVERED
                )
                .count();
    }

    public long getCancelledOrdersCount() {

        return ordersById.values()
                .stream()
                .filter(order ->
                        order.getStatus()
                                == OrderStatus.CANCELLED
                )
                .count();
    }

    public double getPlatformRevenue() {

        return ordersById.values()
                .stream()
                .filter(order ->
                        order.getStatus()
                                == OrderStatus.DELIVERED
                )
                .mapToDouble(Order::getTotal)
                .sum();
    }
    public void addRider(Rider rider) {
        if (rider != null) {
            ridersById.put(rider.getId(), rider);
        }
    }

    public Rider getRider(int riderId) {
        return ridersById.get(riderId);
    }
    }