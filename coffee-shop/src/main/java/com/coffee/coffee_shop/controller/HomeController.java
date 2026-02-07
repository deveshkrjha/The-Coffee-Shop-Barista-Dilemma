package com.coffee.coffee_shop.controller;

import com.coffee.coffee_shop.model.Order;
import com.coffee.coffee_shop.service.OrderQueueService;
import com.coffee.coffee_shop.service.StatsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    private final OrderQueueService orderQueueService;
    private final StatsService statsService;

    public HomeController(OrderQueueService orderQueueService,
                          StatsService statsService) {
        this.orderQueueService = orderQueueService;
        this.statsService = statsService;
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestBody OrderRequest request) {

        int prepTime;
        switch (request.getDrinkType()) {
            case "Cold Brew": prepTime = 1; break;
            case "Espresso":
            case "Americano": prepTime = 2; break;
            case "Cappuccino":
            case "Latte": prepTime = 4; break;
            default: prepTime = 6;
        }

        Order order = new Order(
                "ORDER-" + System.currentTimeMillis(),
                request.getDrinkType(),
                prepTime,
                request.isRegularCustomer()
        );

        orderQueueService.addOrder(order);
        statsService.recordOrderReceived();

        return "✅ Order placed successfully!";
    }

    @GetMapping("/stats")
    public String stats() {
        return """
                📊 Coffee Shop Statistics
                =========================

                %s

                %s

                Current Queue Size: %d
                """.formatted(
                statsService.getPeakStats(),
                statsService.getNonPeakStats(),
                orderQueueService.getQueueSize()
        );
    }
}
