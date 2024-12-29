package com.java.masteringthreads.threadpool.counters;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class CounterDemo {

    interface Counter {
        void increment();
        long get();
    }

    static class SynchronizedCounter implements Counter {
        private long value = 0;
        @Override
        public synchronized void increment() {
            value++;
        }

        @Override
        public synchronized long get() {
            return value;
        }
    }

    static class ReentrantLockCounter implements Counter {
        private final Lock lock;
        private long value;

        public ReentrantLockCounter(boolean fair) {
            lock = new ReentrantLock(fair);
        }

        @Override
        public void increment() {
            lock.lock();
            try {
                value++;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public long get() {
            lock.lock();
            try {
                return value;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0 ; i < 1 ; i++) {
            test(new SynchronizedCounter());
            test(new ReentrantLockCounter(false));
            test(new ReentrantLockCounter(true));
        }
    }

    private static void test(Counter counter) {
        int upto = 100_000_000;
        System.out.println(counter.getClass().getSimpleName());
        Instant start = Instant.now();
        IntStream.range(0, upto)
                //.parallel()
                .forEach(i -> counter.increment());
        if (counter.get() != upto) throw new RuntimeException("impossible");
        System.out.println("Time taken : " + Duration.between(start, Instant.now()).toMillis());
    }
}
