package com.coffee.coffee_shop.service;

import com.coffee.coffee_shop.model.Order;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StatsService {

    // =========================
    // INTERNAL BUCKET
    // =========================
    private static class StatsBucket {
        AtomicInteger received = new AtomicInteger(0);
        AtomicInteger completed = new AtomicInteger(0);
        AtomicInteger timeout = new AtomicInteger(0);
        AtomicLong totalWaitMinutes = new AtomicLong(0);
    }

    private final StatsBucket peakStats = new StatsBucket();
    private final StatsBucket nonPeakStats = new StatsBucket();

    // =========================
    // TIME CHECK
    // =========================
    public boolean isPeakTime() {
        LocalTime now = LocalTime.now();
        return !now.isBefore(LocalTime.of(7, 0))
                && now.isBefore(LocalTime.of(10, 0));
    }

    private StatsBucket activeBucket() {
        return isPeakTime() ? peakStats : nonPeakStats;
    }

    // =========================
    // RECORDING
    // =========================
    public void recordOrderReceived() {
        activeBucket().received.incrementAndGet();
    }

    public void recordOrderCompleted(Order order) {
        StatsBucket bucket = activeBucket();

        bucket.completed.incrementAndGet();
        long wait = order.getWaitingTimeMinutes();
        bucket.totalWaitMinutes.addAndGet(wait);

        if (wait >= 10) {
            bucket.timeout.incrementAndGet();
        }
    }

    // =========================
    // FORMATTERS
    // =========================
    private String formatStats(String title, StatsBucket b) {
        double avgWait =
                b.completed.get() == 0
                        ? 0
                        : (double) b.totalWaitMinutes.get() / b.completed.get();

        return """
                %s
                -------------------
                Orders Received: %d
                Orders Completed: %d
                Avg Wait Time (min): %.2f
                Timeout Orders (>=10 min): %d
                """.formatted(
                title,
                b.received.get(),
                b.completed.get(),
                avgWait,
                b.timeout.get()
        );
    }

    // =========================
    // PUBLIC OUTPUT
    // =========================
    public String getPeakStats() {
        return formatStats("🚨 PEAK HOURS (7:00–10:00)", peakStats);
    }

    public String getNonPeakStats() {
        return formatStats("😌 NON-PEAK HOURS", nonPeakStats);
    }
}
