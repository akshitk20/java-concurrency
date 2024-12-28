package com.java.masteringthreads.threadpool.singleton;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

public class SingletonDemo {
    public static void main(String[] args) {
        Instant start = Instant.now();
        IntStream.range(0,10_000_000)
                .parallel()
                //.forEach(i -> SingletonClassic.getSingletonClassic());
                .forEach(i -> SingletonSynchronized.getSingletonSynchronized());
        Instant end = Instant.now();
        System.out.println("Total time taken : " + Duration.between(start, end).toMillis() + " ms");

    }
}
