package com.java.masteringthreads.threadpool.practical.airportbaggage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.RejectedExecutionException;

public class AirportBaggageImpl implements AirportBaggage {
    private final Deque<Baggage> queue;

    private final Baggage POISON_PILL = new Baggage(0, "");

    public AirportBaggageImpl(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Belt size cannot be less than 1");
        }
        this.queue = new ArrayDeque<>(size);
        for (int i = 0 ; i < size ; i++) {
            new Worker("Worker " + i).start();
        }
    }

    private Baggage take() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) queue.wait();
            Baggage task = queue.removeFirst();
            if (task == POISON_PILL) {
                queue.addLast(task);
                queue.notifyAll();
            }
            System.out.println("Baggage processed " + task);
            return task;
        }
    }

    @Override
    public void submit(Baggage task) {
        synchronized (queue) {
            if (queue.peekLast() == POISON_PILL) throw new RejectedExecutionException("shutdown");
            queue.addLast(task);
            queue.notifyAll();
            System.out.println("Baggage submitted " + task);
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
                    Baggage task = take();
                    if (task == POISON_PILL) {
                        System.out.println("Found poison pill");
                        break;
                    }
                    //task.run();
                    //start();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Completed thread : " +getName());
        }
    }

    public record Baggage(int id, String destination) {}

    public static void main(String[] args) {
        AirportBaggage airportBaggage = new AirportBaggageImpl(2);
        airportBaggage.submit(new Baggage(1, "USA"));
        airportBaggage.submit(new Baggage(2, "INDIA"));
        airportBaggage.shutdown();
    }

}
