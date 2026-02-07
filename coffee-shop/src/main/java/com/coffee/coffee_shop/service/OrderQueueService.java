package com.coffee.coffee_shop.service;

import com.coffee.coffee_shop.model.Order;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderQueueService {

    private PriorityQueue<Order> queue;

    private final Map<Order, Long> arrivalSeq = new HashMap<>();
    private final AtomicLong seqGenerator = new AtomicLong(0);

    private final Map<Order, Integer> skipCount = new HashMap<>();

    public OrderQueueService() {
        this.queue = new PriorityQueue<>(
                (o1, o2) -> {
                    int p1 = calculatePriority(o1);
                    int p2 = calculatePriority(o2);
                    if (p1 != p2) return Integer.compare(p2, p1);
                    return Long.compare(arrivalSeq.get(o1), arrivalSeq.get(o2));
                }
        );
    }

    public synchronized void addOrder(Order order) {
        arrivalSeq.put(order, seqGenerator.incrementAndGet());
        skipCount.put(order, 0);
        queue.add(order);
        System.out.println("📥 Order queued: " + order.getId());
    }

    // Put order back (used for workload balancing)
    public synchronized void requeue(Order order) {
        skipCount.put(order, skipCount.getOrDefault(order, 0) + 1);
        queue.add(order);
    }

    public synchronized Order getNextOrder() {

        // 🚨 Emergency first
        Iterator<Order> it = queue.iterator();
        while (it.hasNext()) {
            Order o = it.next();
            if (o.getWaitingTimeMinutes() >= 10) {
                it.remove();
                cleanup(o);
                System.out.println("🚨 EMERGENCY SERVED: " + o.getId());
                return o;
            }
        }

        PriorityQueue<Order> refreshed =
                new PriorityQueue<>(queue.comparator());
        refreshed.addAll(queue);
        queue = refreshed;

        Order next = queue.poll();
        if (next != null) cleanup(next);
        return next;
    }

    public int getQueueSize() {
        return queue.size();
    }

    private int calculatePriority(Order order) {
        int score = 0;
        long wait = order.getWaitingTimeMinutes();

        score += wait * 4;                 // wait time
        if (order.getPrepTime() <= 2) score += 25;  // quick drink
        if (order.isRegularCustomer()) score += 10; // loyalty
        if (wait >= 8) score += 25;        // urgency

        return score;
    }

    private void cleanup(Order order) {
        arrivalSeq.remove(order);
        skipCount.remove(order);
    }
}
