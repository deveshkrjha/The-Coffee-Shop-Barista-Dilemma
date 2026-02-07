package com.coffee.coffee_shop.service;

import com.coffee.coffee_shop.model.Order;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CoffeeScheduler {

    private final OrderQueueService orderQueueService;
    private final BaristaService baristaService;

    public CoffeeScheduler(OrderQueueService orderQueueService,
                           BaristaService baristaService) {
        this.orderQueueService = orderQueueService;
        this.baristaService = baristaService;
    }

    @PostConstruct
    public void init() {
        System.out.println("⏰ Scheduler started (workload aware)");
    }

    @Scheduled(fixedRate = 2000)
    public void assignOrders() {

        Order order = orderQueueService.getNextOrder();
        if (order == null) return;

        // 🔵 WORKLOAD BALANCING
        if (baristaService.isOverloaded() && order.getPrepTime() > 2) {
            System.out.println("⏳ Overloaded → re-queue slow order: " + order.getId());
            orderQueueService.requeue(order);
            return;
        }

        baristaService.prepareOrder(order);
    }
}
