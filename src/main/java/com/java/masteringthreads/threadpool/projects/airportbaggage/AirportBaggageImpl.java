package com.java.masteringthreads.threadpool.projects.airportbaggage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.RejectedExecutionException;

public class AirportBaggageImpl implements AirportBaggage {
    private final Deque<Runnable> queue;

    private final Runnable POISON_PILL = () -> {};

    public AirportBaggageImpl(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Belt size cannot be less than 1");
        }
        this.queue = new ArrayDeque<>(size);
        for (int i = 0 ; i < size ; i++) {
            new Worker("Worker " + i).start();
        }
    }

    private Runnable take() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) queue.wait();
            Runnable task = queue.removeFirst();
            if (task == POISON_PILL) {
                queue.addLast(task);
                queue.notifyAll();
            }
            return task;
        }
    }

    @Override
    public void submit(Runnable task) {
        synchronized (queue) {
            if (queue.peekLast() == POISON_PILL) throw new RejectedExecutionException("shutdown");
            queue.addLast(task);
            queue.notifyAll();
        }
    }

    @Override
    public void shutdown() {
        synchronized (queue) {
            if (queue.peekLast() != POISON_PILL) {
                submit(POISON_PILL);
            }
        }
    }

    private class Worker extends Thread {
        public Worker(String name) {
            super(name);
        }
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable task = take();
                    if (task == POISON_PILL) {
                        System.out.println("Found poison pill");
                        break;
                    }
                    task.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Completed thread : " +getName());
        }
    }

    public static void main(String[] args) {
        AirportBaggage airportBaggage = new AirportBaggageImpl(1);
        airportBaggage.submit(() -> System.out.println("Task1"));
        airportBaggage.submit(() -> System.out.println("Task2"));
        airportBaggage.shutdown();
    }

}
