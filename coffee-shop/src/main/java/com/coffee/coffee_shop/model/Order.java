package com.coffee.coffee_shop.model;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class Order {

    private String id;
    private String drinkType;
    private int prepTime;                 // in minutes
    private boolean regularCustomer;
    private LocalDateTime orderTime;       // when order was placed

    public Order(String id, String drinkType, int prepTime, boolean regularCustomer) {
        this.id = id;
        this.drinkType = drinkType;
        this.prepTime = prepTime;
        this.regularCustomer = regularCustomer;
        this.orderTime = LocalDateTime.now();   // IMPORTANT
    }

    // ==============================
    // GETTERS
    // ==============================

    // ==============================
    // CORE METHOD FOR PRIORITY
    // ==============================
    public long getWaitingTimeMinutes() {
        return Duration
                .between(orderTime, LocalDateTime.now())
                .toMinutes();
    }

    // Optional: useful for logs
    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", drinkType='" + drinkType + '\'' +
                ", prepTime=" + prepTime +
                ", regularCustomer=" + regularCustomer +
                ", waitingMinutes=" + getWaitingTimeMinutes() +
                '}';
    }
}
