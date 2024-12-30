package com.java.masteringthreads.threadpool.threadcontention;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;

public class ComputeIfAbsent {

    public static void main(String[] args) {
        Instant startTime = Instant.now();
        Map<Boolean, LongAdder> map = new ConcurrentHashMap<>();
        ThreadLocalRandom.current().ints(100_000_000)
                .parallel()
                .forEach(i -> map.computeIfAbsent(i % 2 == 0, v -> new LongAdder())
                        .increment());
        System.out.println("Map : " + map);
        Instant endTime = Instant.now();
        System.out.println("Time taken " + Duration.between(startTime, endTime).toMillis());
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
