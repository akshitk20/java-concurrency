package com.java.masteringthreads.threadpool.projects.restaurantordermanager;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RestaurantManagerImpl implements RestaurantManager {
    private final Deque<Order> queue;
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();

    private final Order POISON_PILL = new Order(-1, "");

    public RestaurantManagerImpl(int size) {
        if (size < 1) throw new IllegalArgumentException("Queue size cannot be less than 1");
        this.queue = new ArrayDeque<>(size);
        for (int i = 0 ; i < size ; i++) {
            new Worker("Worker : " + i).start();
        }
    }

    private Order take() {
        lock.lock();
        try {
            while (queue.isEmpty()) notEmpty.awaitUninterruptibly();
            Order order = queue.removeFirst();
            if (order == POISON_PILL) {
                queue.addLast(order);
                notEmpty.signal();
            }
            return order;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void submit(Order order) {
        lock.lock();
        try {
            if (queue.peekLast() == POISON_PILL) throw new RejectedExecutionException("shutdown");
            queue.addLast(order);
            System.out.println("Submitted Order : " +order);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void shutdown() {
        lock.lock();
        try {
            if (queue.peekLast() != POISON_PILL) {
                submit(POISON_PILL);
            }
        } finally {
            lock.unlock();
        }
    }

    private class Worker extends Thread {
        public Worker(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                Order order = take();
                System.out.println("Processing order : " + order);
                if (order == POISON_PILL) {
                    System.out.println("Found a poison pill ");
                    break;
                }
            }
            System.out.println("Completed thread : " +getName());
        }
    }

    public static void main(String[] args) {
        RestaurantManager restaurantManager = new RestaurantManagerImpl(5);
        restaurantManager.submit(new Order(1, "Pizza"));
        restaurantManager.submit(new Order(2, "Burger"));
        restaurantManager.submit(new Order(3, "Chowmein"));
        restaurantManager.shutdown();
    }
}


