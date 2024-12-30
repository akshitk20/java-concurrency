package com.java.masteringthreads.threadpool.counters;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
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
    static class SynchronizedVolatileCounter implements Counter {
        private volatile long value;
        @Override
        public synchronized void increment() {
            value++;
        }

        @Override
        public long get() {
            return value;
        }
    }
    static class AtomicCounter implements Counter {
        private final AtomicLong atomicLong = new AtomicLong();
        @Override
        public void increment() {
            atomicLong.getAndIncrement();
        }

        @Override
        public long get() {
            return atomicLong.get();
        }
    }

    static class LongAdderCounter implements Counter {
        private final LongAdder longAdder = new LongAdder();
        @Override
        public void increment() {
            longAdder.increment();
        }

        @Override
        public long get() {
            return longAdder.longValue();
        }
    }

    public static void main(String[] args) {
        for (int i = 0 ; i < 1 ; i++) {
            test(new SynchronizedCounter());
            test(new ReentrantLockCounter(false));
            test(new ReentrantLockCounter(true));
            test(new SynchronizedVolatileCounter());
            test(new AtomicCounter());
            test(new LongAdderCounter());
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
