package com.coffee.coffee_shop.service;

import com.coffee.coffee_shop.model.Order;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BaristaService {

    private static final int TOTAL_BARISTAS = 3;

    private ExecutorService baristaPool;
    private final AtomicInteger activeBaristas = new AtomicInteger(0);

    private final StatsService statsService;

    public BaristaService(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostConstruct
    public void init() {
        baristaPool = Executors.newFixedThreadPool(TOTAL_BARISTAS);
        System.out.println("👩‍🍳👨‍🍳👩‍🍳 Baristas are ready");
    }

    public boolean isOverloaded() {
        return activeBaristas.get() >= TOTAL_BARISTAS;
    }

    public void prepareOrder(Order order) {

        activeBaristas.incrementAndGet();

        baristaPool.submit(() -> {
            String baristaName = Thread.currentThread().getName();

            System.out.println("☕ " + baristaName +
                    " started: " + order.getId() +
                    " | Prep: " + order.getPrepTime() + " min");

            try {
                // ✅ FIX: minutes → milliseconds
                Thread.sleep(order.getPrepTime() * 60 * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                activeBaristas.decrementAndGet();
            }

            statsService.recordOrderCompleted(order);

            System.out.println("✅ " + baristaName +
                    " completed: " + order.getId());
        });
    }

    @PreDestroy
    public void shutdown() {
        baristaPool.shutdown();
    }
}
